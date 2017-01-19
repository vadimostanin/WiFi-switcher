local timeSyncAvailable = false
local timerTimeSyncId = 1
local timerCheckScheduleId = 2
local timerCheckConnectionStateId = 3
local timerLedStatusId = 4
local timerGarbageCollectionId = 5
local timeSntpSyncAcquired = false
local wifiConnectedToAP = nil

local function timeSync()
    if false == timeSntpSyncAcquired then
        timeSntpSyncAcquired = true
        -- print( "time sync start" )
        sntp.sync("pool.ntp.org", function() -- success
                -- print( "INFO: Time Synchronized (via sntp). Current timestamp: "..rtctime.get() )
                local tm = rtctime.epoch2cal( rtctime.get() )
                -- print(string.format("%04d/%02d/%02d %02d:%02d:%02d", tm["year"], tm["mon"], tm["day"], tm["hour"], tm["min"], tm["sec"]))
                timeSyncAvailable = true
                timeSntpSyncAcquired = false
            end, 
            function ()
                timeSyncAvailable = false
                timeSntpSyncAcquired = false
            end)
    end
end

control_file_path = "controlstate.txt"
function controlStateWriteFile( value )
    if nil == file.open( control_file_path, "w+" ) then
        return
    end
    file.write( tostring( value ) )
    file.close()
end

function controlStateReadFile()
    if nil == file.open( control_file_path, "r" ) then
        return 0
    end
    value = file.read( 1 )
    file.close()
    return tonumber( value )
end

function buttonSwicthAP_STA_Setup()
  gpio.mode( buttonAP_Station, gpio.INPUT )
  gpio.mode( buttonAP_Station, gpio.INT, gpio.PULLUP )
  -- gpio.trig( buttonAP_Station, "down", processButtonAPClicked )
  -- gpio.trig( buttonAP_Station, "up", processButtonSTAClicked )

  gpio.trig( buttonAP_Station, "both", processButtonAP_STAClicked )
end

local function checkSchedule()
    -- print( "checkSchedule enter" )
    local tm = rtctime.epoch2cal( rtctime.get() )
    -- print(string.format("%04d/%02d/%02d %02d:%02d:%02d", tm["year"], tm["mon"], tm["day"], tm["hour"], tm["min"], tm["sec"]))
    local currDate = Date.create( tm["year"], tm["mon"], tm["day"] )
    local currTime = Time.create( tm["hour"], tm["min"] )
    for i, schedItem in ipairs( gSchedulerManager:get() ) do
        -- print( "schedItem.time=" .. schedItem.time:toString() )
        if schedItem.period == string_schedule_ones then
            if true == currDate:equals( schedItem.date ) and true == currTime:equals( schedItem.time ) then
                -- print( "schedItem.period == ones" )
                -- print( "currTime=" .. currTime:toString() .. ", currDate=" .. currDate:toString() .. ", schedItem.time=" .. schedItem.time:toString() .. ", schedItem.date=" .. schedItem.date:toString() )
                processScheduleAction( schedItem.action )
                gSchedulerManager:remove( gSchedulerManager:get(), i )
            end
        elseif schedItem.period == string_schedule_everyday then
            -- print( "schedItem.period == everyday" )
            -- print( "currTime=" .. currTime:toString() .. ", schedItem.time=" .. schedItem.time:toString() )
            if true ==  currTime:equals( schedItem.time ) then
                processScheduleAction( schedItem.action )
                table.remove( gSchedulerManager:get(), i )
            end
        end
    end
    -- print( "checkSchedule leave" )
end

local function timerTimerAndScheduleStart()
    tmr.alarm( timerTimeSyncId, 10000, tmr.ALARM_AUTO, 
            function()
                if false == timeSyncAvailable then
                    timeSync()
                    collectgarbage()
                    return
                else
                    tmr.stop( timerTimeSyncId )
                end
            end)

    tmr.alarm( timerCheckScheduleId, 10000, 1, 
            function()
                if false == timeSyncAvailable then
                    return
                end
                if nil ~= gSchedulerManager then
                    checkSchedule()
                    collectgarbage()
                end
            end)
    tmr.alarm( timerGarbageCollectionId, 1000, 1, 
            function()
                -- collectgarbage()
                collectgarbage('collect')
            end)
end

function timerLedStart()
    tmr.alarm( timerLedStatusId, 500, 1, 
            function()
                if nil == wifiConnectedToAP then
                    -- print( "Led EndUserSetup" )
                    gpio.write( statusLedPin, gpio.HIGH )--EndUserSetup
                elseif true == wifiConnectedToAP then
                    -- print( "Led wifi connected" )
                    gpio.write( statusLedPin, gpio.LOW )--Wifi connected
                elseif false == wifiConnectedToAP then
                    -- print( "Led not wifi connected" )
                    gpio.write( statusLedPin, ( gpio.HIGH == gpio.read( statusLedPin ) ) and gpio.LOW or gpio.HIGH )--Wifi not connected
                end
            end)
end

function endUserSetupStart()
    wifi.setmode(wifi.STATIONAP)
    wifi.ap.config({ssid="MyPersonalSSID", auth=wifi.OPEN})
    enduser_setup.manual(true)
    enduser_setup.start(
      function()
        print("Connected to wifi as:" .. wifi.sta.getip())
      end,
      function(err, str)
        print("enduser_setup: Err #" .. err .. ": " .. str)
      end
    )
end

local function registerServer()
    if udp ~= nil then
      udp:close()
      udp = nil
    end
--    if udp2 ~= nil then
--      udp2:close()
--    end
    if tcp ~= nil then
      tcp:close()
      tcp = nil
    end
    collectgarbage()
    -- print("before create server")
    udp = net.createServer(net.UDP)
    udp:on("receive",
                function(socket, request)
                    -- print(request)
                    -- print(wifi.sta.getip())
                    local is_discover = string.find( request, "discover" ) ~= nil
                        
                    if is_discover then
                        -- print(wifi.sta.getmac())
                        -- print(node.chipid())
                        local response = "id=" .. node.chipid() .. ",type=switcher"
                        socket:send( response )
--                        socket.close( udp )
                    end
                end)
    udp:listen(33249)

    -- a udp server
--    udp2=net.createServer(net.UDP)
--    udp2:on("receive",function(s,c) print(c) end)
--    udp2:listen(5683)

    -- Start a simple http server
    tcp = net.createServer(net.TCP)
    tcp:listen( 33248,function( conn )
      conn:on("receive",function( socket,request )
        local _, _, method, path, vars = string.find( request, "([A-Z]+) (.+)?(.+) HTTP" );
        if(method == nil)then
            _, _, method, path = string.find(request, "([A-Z]+) (.+) HTTP");
        end

        -- print( "request=" .. request )
        if nil ~= method then
            -- print( "method=" .. method )
        end
        if nil ~= path then
            -- print( "path=" .. path )
        end
        if nil ~= vars then
            -- print( "vars=" .. vars )
        end

        if nil ~= httpGetContentType( path ) then
            if true then
                local requestFile = httpGetFileRequest( path );
                -- print( "[Sending file " .. requestFile .. "]" );
                local sender = HttpFileSender.create( socket, requestFile )
                sender:send( "HTTP/1.1 200 OK\r\nContent-Type: " .. httpGetContentType( path ) .. "\r\n\r\n" );
            end
            collectgarbage()
--            socket:close();
        elseif nil ~= string.find( path, "/setstate" ) then
            processSetState( socket, path, vars )
        elseif nil ~= string.find( path, "/getstate" ) then
            processGetState( socket )
        elseif nil ~= string.find( path, "/schedule" ) then
            processSchedule( socket, path, vars )
        elseif nil ~= string.find( path, "/getschedule" ) then
            processGetScheduleList( socket, path, vars )
        elseif path == "/" then
            -- print( "send generated html" )
            sendFullwebPage( socket )
            collectgarbage('collect')
        else
            -- print("[File "..path.." not found]")
            socket:send( "HTTP/1.1 404 Not Found\r\n\r\n" )
            socket:close()
            collectgarbage('collect')
        end
        
      end)
      conn:on("sent",function(socket)  end)--socket:close()
    end)
end

-- function createEnduserSetupHtml()
--     if nil == file.open("enduser_setup.html") then
--         print("failed create file enduser_setup.html")
--         return
--     end
--     file.write( "<html>Hello World</html>" );
--     print( "success create file enduser_setup.html" )
--     file.close()
-- end

function connectAsAStation()
    wifi.setmode(wifi.STATION)
--    wifi.sta.config("laptop","q1w2e3r4")
--    wifi.sta.config("ITPwifi","_RESTRICTED3db@ea")
    -- wifi.sta.config("This is","*parusex*")
    -- wifi.sta.connect()
    wifi.sta.autoconnect(1)
    
    -- function listap(t)
    --     for k,v in pairs(t) do
    --         print(k.." : "..v)
    --     end
    -- end
    -- wifi.sta.getap(listap)
    
    tmr.alarm( timerCheckConnectionStateId, 1000, 1, 
        function()
            if wifi.sta.getip() == nil then
                -- print("Connecting to AP...")
                if true == wifiConnectedToAP then

                end
                wifiConnectedToAP = false
            elseif false == wifiConnectedToAP then
                wifiConnectedToAP = true
                -- print('IP: ',wifi.sta.getip())
                registerServer()
                -- registerServer = nil
                -- print( "heap1=" .. node.heap() )
                if nil ~= timerTimerAndScheduleStart then -- only one time expected
                    timerTimerAndScheduleStart()
                    timerTimerAndScheduleStart = nil
                end
                -- print( "heap2=" .. node.heap() )
        --        tmr.stop( timerCheckConnectionStateId )
            end
        end)
end
