function processScheduleAction( action )
    -- print( "processScheduleAction" )
    if action == string_off_value then
        -- print( "off" )
        gpio.write( controlPin, gpio.LOW )
    elseif action == string_on_value then
        -- print( "on" )
        gpio.write( controlPin, gpio.HIGH )
    end
end

function processSetState( socket, path, vars )
--    print( "processSetState" )
    local web = nil
    local switch = nil
    for var in string.gmatch( vars, "(([a-zA-Z_=0-9]+)&?)" ) do
--        print( "var=" .. var )
        local _, _, var_key, var_value = string.find( var, "(%w+)=(%w+)" )
--            print( "var_key=" .. var_key )
--            print( "var_value=" .. var_value )
        if var_key == "switch" then
--                print( "switch=" .. var_value )
            switch = var_value
        elseif var_key == "web" then
--                print( "web=" .. var_value )
            web = var_value
        end
    end
--    print( "switch=" .. switch )
    if switch == string_on_value then
--        print( "on" )
        gpio.write( controlPin, gpio.HIGH )
        controlStateWriteFile( gpio.HIGH )
    elseif switch == string_off_value then
--        print( "off" )
        gpio.write( controlPin, gpio.LOW )
        controlStateWriteFile( gpio.LOW )
    end
--            socket:send( "ok" )
    if web == "1" then
        local html = generateWebHtml( gpio.read( controlPin ), node.chipid() )
        httpsendfull_3( socket, html )
    else
        local html = generateEmptyHtml()
        httpsendfull_3( socket, html )
    end
    collectgarbage('collect')
end

function processGetState( socket )
--    print( "processGetState" )
    if true then
        local html = generateGetStateHtml( gpio.read( controlPin ), node.chipid() )
        httpsendfull_3( socket, html )
    end
    collectgarbage('collect');
end

function sendFullwebPage( socket )
--    local 
    local sender = HttpSender.create( socket )
    local funcSendByPart = function( part, maxpart )
        -- print( "funcSendByPart part=" .. tostring( part ) .. ", maxpart=" .. tostring( maxpart ) .. " enter" )
        local html = generateWebHtml( gpio.read( controlPin ), node.chipid(), part )
        -- httpsendfull_3( socket, html )

        if part < maxpart then
            -- print( "if part < maxpart then" )
            sender:setOnSent( 
                function()
                    collectgarbage();
                    funcSendByPart( part + 1, maxpart )
                end)
            collectgarbage();
            -- print( "call sender:send( html, false )" )
            sender:send( html, false ) -- not last send
        else
            sender:setOnSent( 
                function()
                    sender = nil
                    socker:close()
                    collectgarbage();
                end)
            collectgarbage();
            sender:send( html, true ) -- last send
            collectgarbage();
        end
    end
    funcSendByPart( 1, 4 )
    collectgarbage();
end

function processSchedule( socket, path, vars )
    -- print( "processSchedule" )
    -- return
--path=/schedule=1
--vars=scheduler_type=ones&schedule_action=1&schedule_date=&schedule_time=
    if true then
        local scheduler_period = nil
        local schedule_action = nil
        local schedule_date = nil
        local schedule_time = nil
        local web = nil
        local switch = nil
        for var in string.gmatch( vars, "(([a-zA-Z_%%=0-9-]+)&?)" ) do
           -- print( "var=" .. var )
            local _, _, var_key, var_value = string.find( var, "([a-zA-Z_%%=0-9-]+)=([a-zA-Z_%%:=0-9-]+)" )
               -- print( "var_key=" .. var_key )
               -- print( "var_value=" .. var_value )
            if var_key == "schedule_period" then
                -- print( "schedule_period=" .. var_value )
                scheduler_period = var_value
            elseif var_key == "schedule_action" then
                -- print( "schedule_action=" .. var_value )
                schedule_action = var_value
            elseif var_key == "schedule_date" then
                -- print( "schedule_date=" .. var_value )
                local _, _, year, mon, day = string.find( var_value, "([0-9]*)-([0-9]*)-([0-9]*)")
                schedule_date = Date.create( year, mon, day )
            elseif var_key == "schedule_time" then
                -- print( "schedule_time=" .. var_value )
                local _, _, hour, min = string.find( var_value, "([0-9]*)%%3A([0-9]*)")
                -- print( "hour=" .. hour .. ", min=" .. min )
                schedule_time = Time.create( hour, min )
            end
        end
    
        if nil ~= scheduler_period or nil ~= schedule_action or nil ~= schedule_date or nil ~= schedule_time then
           -- print( "scheduler_period=" .. scheduler_period )
           -- print( "schedule_action=" .. schedule_action )
           -- print( "schedule_date=" .. schedule_date:toString() )
           -- print( "schedule_time=" .. schedule_time:toString() )

            if scheduler_period == string_schedule_ones then
               local schedItem = SchedulerItem.createOnes( scheduler_period, schedule_action, schedule_date, schedule_time )
               gSchedulerManager:add( schedItem )
            elseif scheduler_period == string_schedule_everyday then
               local schedItem = SchedulerItem.createEveryday( scheduler_period, schedule_action, schedule_time )
               gSchedulerManager:add( schedItem )
            end

            gSchedulerManager:save()
            -- print( "memory2=" .. node.heap() )
            -- print( gSchedulerManager:count() )
        end
    end
--     print( "memory2=" .. node.heap() )
--     collectgarbage();
--     print( "memory3=" .. node.heap() )
    collectgarbage();
--     print( "memory4=" .. node.heap() )
    
    if true then
    -- print( "memory5=" .. node.heap() )
        -- local html = generateWebHtml_2( gpio.read( controlPin ), node.chipid() )
        local html = generateEmptyHtml()
        -- print( "memory6=" .. node.heap() )
        httpsendfull_3( socket, html )
        -- sendFullwebPage( socket )
        -- socket:close()
        -- print( "memory7=" .. node.heap() )
    end
--     print( "memory8=" .. node.heap() )
    collectgarbage();
--     print( "memory9=" .. node.heap() )
end

function processGetScheduleList( socket, path, vars )
    if true then
        local html = generateScheduleListHtml()
        httpsendfull_3( socket, html )
    end
    collectgarbage();
end

function processButtonAP_STAClicked( level )
    node.restart()
end














