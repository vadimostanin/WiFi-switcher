Date = {}
Date.__index = Date
function Date.create( stryear, strmon, strday, strhour, strmin )
    local dateObj = {}
    setmetatable( dateObj, Date )
    dateObj.year = tonumber( stryear )
    dateObj.mon = tonumber ( strmon )
    dateObj.day = tonumber( strday )
    return dateObj
end

function Date:equals( yearInt, monInt, dayInt )
    if self.year == yearInt and self.mon == monInt and self.day == dayInt then
        return true
    end
    return false
end

function Date:toString()
    return self.year .. "-" .. self.mon .. "-" .. self.day
end

Time = {}
Time.__index = Time
function Time.create( strhour, strmin )
    local timeObj = {}
    setmetatable( timeObj, Time )
    timeObj.hour = tonumber( strhour )
    timeObj.min = tonumber( strmin )
    return timeObj
end

function Time:equals( hourInt, minInt )
    if self.hour == hourInt and self.min == minInt then
        return true
    end
    return false
end

function Time:equals( compTimeObj )
    if self.hour == compTimeObj.hour and self.min == compTimeObj.min then
        return true
    end
    return false
end

function Time:toString()
    return self.hour .. ":" .. self.min
end

SchedulerItem = {}
SchedulerItem.__index = SchedulerItem

local schedule_store_file_path = "schedule.txt"

function SchedulerItem.createOnes( period, action, date, time )
--    scheduleItem = ShedulerItem.create( period, action, time )
--    print( "ShedulerItem.create enter date" )
    local scheduleItem = {}
    setmetatable( scheduleItem, SchedulerItem )

--    print( "period=" .. period )
--    print( "action=" .. action )
--    print( "time=" .. time )
--    print( "date=" .. date )

    scheduleItem.period = period
    scheduleItem.action = action
    scheduleItem.time = time
    scheduleItem.date = date
    return scheduleItem
end

function SchedulerItem.createEveryday( period, action, time )
    local scheduleItem = {}
    setmetatable( scheduleItem, SchedulerItem )

    scheduleItem.period = period
    scheduleItem.action = action
    scheduleItem.time = time
--    print( "ShedulerItem.create enter" )
    return scheduleItem
end

function SchedulerItem.parse( schedLine )
    local SchedulerItemInternalParse = 
        function ( schedLine )
            local scheduler_period = nil
            local schedule_action = nil
            local schedule_date = nil
            local schedule_time = nil
            local web = nil
            local switch = nil
            for var in string.gmatch( schedLine, "(([a-zA-Z_%%:=0-9-]+)&?)" ) do
                -- print( "var=" .. var )
                for var_key, var_value in string.gmatch( var, "([a-zA-Z_%%=0-9-]+)=([a-zA-Z_%%:=0-9-]+)" ) do
                --            print( "var_key=" .. var_key )
                --            print( "var_vreturn alue=" .. var_value )
                    if var_key == "period" then
                --                print( "scheduler_period=" .. var_value )
                        scheduler_period = var_value
                    elseif var_key == "action" then
                --                print( "schedule_action=" .. var_value )
                        schedule_action = var_value
                    elseif var_key == "date" then
                --                print( "schedule_date=" .. var_value )
                        local _, _, year, mon, day = string.find( var_value, "([0-9]*)-([0-9]*)-([0-9]*)")
                        schedule_date = Date.create( year, mon, day )
                    elseif var_key == "time" then
                --                print( "schedule_time=" .. var_value )
                        local _, _, hour, min = string.find( var_value, "([0-9]*):([0-9]*)")
                        -- print( "hour=" .. hour )
                        -- print( "min=" .. min )
                        schedule_time = Time.create( hour, min )
                    end
                end
            end
            if scheduler_period == string_schedule_ones then
                return SchedulerItem.createOnes( scheduler_period, schedule_action, schedule_date, schedule_time )
            elseif scheduler_period == string_schedule_everyday then
                return SchedulerItem.createEveryday( scheduler_period, schedule_action, schedule_time )
            end
            return nil
        end
    return SchedulerItemInternalParse( schedLine )
end

SchedulerManager = {}
SchedulerManager.__index = SchedulerManager


function SchedulerManager.create()
    local createManagerInternal = 
        function ()
            local schedulerManager = {}
            setmetatable( schedulerManager, SchedulerManager )

            schedulerManager.schedList = {}

            return schedulerManager
    end
    return createManagerInternal()
end

--[[
function SchedulerManager.create()
    local schedulerManager = {}
    setmetatable( schedulerManager, SchedulerManager )

    schedulerManager.schedList = {}

    return schedulerManager
end
]]

function SchedulerManager:remove( schedItem )
    table.insert( self.schedList, schedItem )
end

function SchedulerManager:add( schedItem )
    table.insert( self.schedList, schedItem )
end

function SchedulerManager:count()
    return #self.schedList
end

function SchedulerManager:get( index )
    return self.schedList[ index ]
end

function SchedulerManager:get()
    return self.schedList
end

function SchedulerManager:save()
    local saveInternal = 
        function ( schedList )
        if nil ~= file.open( schedule_store_file_path, "w+" ) then
            for i, shedItem in ipairs( schedList ) do
    --            print( "period=" .. shedItem.period )
    --            print( "action=" .. shedItem.action )
    --            print( "time=" .. shedItem.time )
    --            print( "date=" .. shedItem.date )
                if shedItem.period == string_schedule_ones then
                    local shedItemLine = "period=" .. shedItem.period .. "&action=" .. shedItem.action .. "&date=" .. shedItem.date:toString() .. "&time=" .. shedItem.time:toString()
                    file.writeline( shedItemLine )
                elseif shedItem.period == string_schedule_everyday then
                    local shedItemLine = "period=" .. shedItem.period .. "&action=" .. shedItem.action .. "&time=" .. shedItem.time:toString()
                    file.writeline( shedItemLine )
                end
            end
            
            file.close()
        end
        end
    saveInternal( self.schedList )
end

function SchedulerManager:load()
    local loadInternal = 
        function ()
            local schedListLoaded = {}
        --    print( "SchedulerManager:load() enter" )
            if nil == file.open( schedule_store_file_path, "r" ) then
                -- print( "SchedulerManager:load, open file failed" )
                return
            end
        -- --    print( "SchedulerManager:load() 1" )
            repeat
                local schedLine = file.readline()
        -- --        print( "SchedulerManager:load() schedLine=nil" )
                if nil == schedLine then
                    -- print( "SchedulerManager:load, break" )
                    break
                end
                -- print( "SchedulerManager:load, schedLine=" .. schedLine )
        -- --        print( "SchedulerManager:load() schedLine=" .. schedLine )
                local schedItem = SchedulerItem.parse( schedLine )
        -- --        print( "SchedulerManager:load() 3" )
                table.insert( schedListLoaded, schedItem )
        --        print( "SchedulerManager:load() 4" )
            until( false )
            file.close()
        --    print( "SchedulerManager:load() leave" )
            return schedListLoaded
        end
    local schedListLoaded = loadInternal()
    if nil == schedListLoaded then
        return
    end
    for i, schedItem in ipairs( schedListLoaded ) do
        table.insert( self.schedList, schedItem )
    end
end








