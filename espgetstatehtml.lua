function generateEmptyHtml()
    local html = [[HTTP/1.1 200 OK
Content-Type: text/html

]]
    return html
end

function generateGetStateHtml( current_state, chip_id )
    local html = [[HTTP/1.1 200 OK
Content-Type: text/html

<!DOCTYPE HTML>
<html>
<div class="device_type">Switcher</div>
<div class="device_id">]] .. chip_id .. [[</div>
<div class="device_state"]] 
.. ( ( 1 == current_state ) and "style=\"color: green;\">ON" or "style=\"color: red;\">OFF" ) .. 
[[</div>
  </html>
]]
    -- print( "html_len=" .. #html )
    return html
end

function generateScheduleListHtml()
    -- print( "generateWebHtml: enter, current_state=" .. current_state .. ", chip_id=" .. chip_id .. ", part=" .. part )
    local html = [[HTTP/1.1 200 OK
Content-Type: text/html

<!DOCTYPE HTML><html>]]
    if nil == gSchedulerManager then

    end
    for i, schedItem in ipairs( gSchedulerManager:get() ) do
        html = html .. [[<div class="schedule_item">]]
        if schedItem.period == string_schedule_ones then
            html = html .. [[<div class="action">]] .. schedItem.action .. [[</div>
            <div class="period">]] .. schedItem.period .. [[</div>
            <div class="date">]] .. schedItem.date:toString() .. [[</div>
            <div class="time">]] ..schedItem.time:toString() .. [[</div>]]
        elseif schedItem.period == string_schedule_everyday then
            html = html .. [[<div class="action">]] .. schedItem.action .. [[</div>
            <div class="period">]] .. schedItem.period .. [[</div>
            <div class="time">]] ..schedItem.time:toString() .. [[</div>]]
        end
        html =  html .. [[</div>]]
    end
    html = html .. [[</html>]]
    -- print( html )
    return html
end

function generateWebHtml( current_state, chip_id, part )
    -- print( "generateWebHtml: enter, current_state=" .. current_state .. ", chip_id=" .. chip_id .. ", part=" .. part )
    local schedHtml = ""
    for i, schedItem in ipairs( gSchedulerManager:get() ) do
        if schedItem.period == string_schedule_ones then
            schedHtml = schedHtml .. "<option>" .. schedItem.action .. " " .. schedItem.period .. " " .. schedItem.date:toString() .. " " ..schedItem.time:toString() .. "</option>"
        elseif schedItem.period == string_schedule_everyday then
            schedHtml = schedHtml .. "<option>" .. schedItem.action .. " "  .. schedItem.period .. " " .. schedItem.time:toString() .. "</option>"
        end
    end
    local function getTime()
        local tm = rtctime.epoch2cal( rtctime.get() )
        local sTime = string.format("%04d/%02d/%02d %02d:%02d:%02d", tm["year"], tm["mon"], tm["day"], tm["hour"], tm["min"], tm["sec"])
        return sTime
    end
    local html = ""
    if 1 == part then
        html = [[HTTP/1.1 200 OK
Content-Type: text/html

<!DOCTYPE HTML>
<html>
<head>
<script src="http://www.w3schools.com/lib/w3data.js"></script>
<script type="text/javascript" src="espwebhtml.js" ></script
</head>
<body onload="createSchedulerFormOnes()">
<table style="width: 200px; height: 200px; margin-left: auto; margin-right: auto;">
<tbody>
<tr>
<td>Type</td>
<td class="device_type">Switcher</td>
</tr>]]
    elseif 2 == part then
        -- print( "generateWebHtml: part==2, before html_len=" .. #html )
        html = [[
<tr>
<td>ID</td>
<td><div class="device_id">]]
.. chip_id .. 
[[
</div></td>
</tr>
<tr>
<td>State</td>
<td><div class="device_state"]]
.. ( ( 1 == current_state ) and "style=\"color: green;\">ON" or "style=\"color: red;\">OFF" ) ..
[[
</div></td>
</tr>
<tr>
<td>Time</td>
<td><div class="device_time">]]
.. getTime() .. 
[[
</div></td>
</tr>

<tr>
<td>Turn on</td>
]]
    -- print( "generateWebHtml: html_len=" .. #html )
    elseif 3 == part then
        html = [[
<td><form action="/setstate">
<input type="submit" name="submit" value=]]
.. ( ( 1 == current_state ) and "\"OFF\"" or "\"ON\"" ) ..
[[/>
<input type="hidden" name="web" value="1"/>
<input type="hidden" name="switch" value=]]
.. ( ( 1 == current_state ) and "\"0\"" or "\"1\"" ) ..
[[/>
</form></td>
</tr>
</tbody>
</table>]]
    elseif 4 == part then
        html = [[
<div w3-include-html="scheduler.html"></div>
<script>
    w3IncludeHTML();
</script>
]]
        if nil ~= schedHtml then
            html = html .. [[
<table>
<tbody>
<tr>
            <td>
                Schuduler:
            </td>
            <td>
                <select name="sometext" multiple="multiple">]] ..
schedHtml
..[[</select>
            </td>
        </tr>
</tbody>
</table>
]]
        end

        html = html .. [[
</body>
</html>
]]
    end
    -- print( "generateWebHtml: leave" )
    return html

end

-- function generateWebHtml_2( current_state, chip_id, part )
--     local schedHtml = ""
--     for i, schedItem in ipairs( gSchedulerManager:get() ) do
--         if schedItem.period == string_schedule_ones then
--             schedHtml = schedHtml .. "<option>" .. schedItem.action .. " " .. schedItem.period .. " " .. schedItem.date:toString() .. " " ..schedItem.time:toString() .. "</option>"
--         elseif schedItem.period == string_schedule_everyday then
--             schedHtml = schedHtml .. "<option>" .. schedItem.action .. " "  .. schedItem.period .. " " .. schedItem.time:toString() .. "</option>"
--         end
--     end
--     local function getTime()
--         local tm = rtctime.epoch2cal( rtctime.get() )
--         local sTime = string.format("%04d/%02d/%02d %02d:%02d:%02d", tm["year"], tm["mon"], tm["day"], tm["hour"], tm["min"], tm["sec"])
--         return sTime
--     end
--     local html = ""
--     if 1 == part then
--         html = [[HTTP/1.1 200 OK
-- Content-Type: text/html

-- <!DOCTYPE HTML>
-- <html>
-- <head>
-- <script type="text/javascript">
-- function createRadioONOFF( id, name, value, text, checked )
-- {
-- var radioObj = createInput( id, name, "radio" )
-- radioObj.checked = checked;
-- radioObj.value = value;
-- radioObj.innerHTML = text;
-- return radioObj;
-- }

-- function createInput( id, name, type )
-- {
-- //create input element
-- var inputObj = document.createElement( "input" );
-- inputObj.name = name;
-- inputObj.id = id;
-- inputObj.type = type;

-- return inputObj;
-- }]]
--     elseif 2 == part then
--         html = [[
-- function clearChilds( id )
-- {
-- var myDiv = document.getElementById( id );
-- while( myDiv.firstChild )
-- {
-- myDiv.removeChild( myDiv.firstChild );
-- }
-- }

-- function createSchedulerFormOnes()
-- {
--     clearChilds( "schedule_form" );

--     //Create array of options to be added
--     var array = ["ON","OFF"];
--     var myDiv = document.getElementById( "schedule_form" );
    
--     var labelDate = document.createElement("label"); 
--     labelDate.for="schedule_date"; 
--     labelDate.innerHTML = "<h3>Date:</h3>";
--     myDiv.appendChild( labelDate );
-- ]]
--     elseif 3 == part then
--         html = [[
--     var scheduleDate = createInput( "schedule_date", "schedule_date", "date" );
--     myDiv.appendChild( scheduleDate );
    
    
--     var labelTime = document.createElement("label"); 
--     labelTime.for="schedule_date"; 
--     labelTime.innerHTML = "<h3>Time:</h3>";
--     myDiv.appendChild( labelTime );
    
--     var scheduleTime = createInput( "schedule_time", "schedule_time", "time" );
--     myDiv.appendChild( scheduleTime );
-- }]]
--     elseif 4 == part then
--         html = [[
-- function createSchedulerFormEveryday()
-- {
--     clearChilds( "schedule_form" );
--     var scheduleTime = createInput( "schedule_time", "schedule_time", "time" );
--     var myDiv = document.getElementById( "schedule_form" );
--     var labelTime = document.createElement("label"); 
--     labelTime.for="schedule_date"; 
--     labelTime.innerHTML = "<h3>Time:</h3>";
--     myDiv.appendChild( labelTime );
    
--     myDiv.appendChild( scheduleTime );
-- }
-- </script
-- </head>
-- <body onload="createSchedulerFormOnes()">]]
--     elseif 5 == part then
--         html = [[
-- <table style="width: 200px; height: 200px; margin-left: auto; margin-right: auto;">
-- <tbody>
-- <tr>
-- <td>Type</td>
-- <td class="device_type">Switcher</td>
-- </tr>
-- <tr>
-- <td>ID</td>
-- <td><div class="device_id">]]
-- .. chip_id .. 
-- [[
-- </div></td>
-- </tr>
-- <tr>
-- <td>State</td>
-- <td><div class="device_state"]] 
-- .. ( ( 1 == current_state ) and "style=\"color: green;\">ON" or "style=\"color: red;\">OFF" ) .. 
-- [[
-- </div></td>
-- </tr>
-- <tr>
-- <td>Turn on</td>
-- <td><form action="/setstate">]]
--     elseif 6 == part then
--         html = [[
-- <input type="submit" name="submit" value=]]
-- .. ( ( 1 == current_state ) and "\"OFF\"" or "\"ON\"" ) .. 
-- [[/>
-- <input type="hidden" name="web" value="1"/>
-- <input type="hidden" name="switch" value=]]
-- .. ( ( 1 == current_state ) and "\"0\"" or "\"1\"" ) ..
-- [[/>
-- </form></td>
-- </tr>
-- <tr>
-- <td>
-- <div>Scheduling</div>
-- </td>
-- <td>
-- <form action="/schedule">
-- <h3>Periods:</h3>
-- <input type="radio" id="scheduler_period_ones" name="scheduler_period" value="ones" checked onclick="createSchedulerFormOnes()">]]
--     elseif 7 == part then
--         html = [[
-- <label for="scheduler_period_ones" >Ones</label><br/>
-- <input type="radio" id="scheduler_period_everyday" name="scheduler_period" value="everyday" onclick="createSchedulerFormEveryday()">
-- <label for="scheduler_period_everyday" >Everyday</label><br>
-- <h3>Actions:</h3>
-- <input type="radio" id="schedule_action_on" name="schedule_action" value="1" checked onclick="createSchedulerFormOnes()">
-- <label for="schedule_action_on" ><font color=green>ON</font></label><br/>]]
--     elseif 8 == part then
--         html = [[
-- <input type="radio" id="schedule_action_off" name="schedule_action" value="0" onclick="createSchedulerFormEveryday()">
-- <label for="schedule_action_off" ><font color=red>OFF</font></label><br>
-- <div>
-- <div id="schedule_form" />
-- </div>
-- </br>
-- </br>
-- <input type="submit" value="Add"/>
-- </form>
-- </td>
-- </tr>
-- </tbody>
-- </table>
-- ]]
--         if nil ~= schedHtml then
--             html = html .. [[
-- <table>
-- <tbody>
-- <tr>
-- <td>
-- Schuduler:
-- </td>
-- <td>
-- <select name="sometext" multiple="multiple">]] ..
-- schedHtml
-- ..[[</select>
-- </td>
-- </tr>
-- </tbody>
-- </table>
-- </body>
-- </html>
-- ]]
--     end
-- end
--     print( "generateWebHtml: leave" )
--     return html
-- end