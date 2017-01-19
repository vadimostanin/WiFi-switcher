-- https://nodemcu.readthedocs.io/en/master/en/modules/gpio/
-- pin GPIO12 is 6

uart.setup( 0, 115200, 8, 0, 1 )

node.stripdebug(3)

-- print("Start ESP8266")

dofile( "constants.lc" )
dofile( "actionsprocess.lc" )
dofile( "estbehaviour.lc" )

gpio.mode( controlPin, gpio.OUTPUT )

gpio.mode( statusLedPin, gpio.OUTPUT, gpio.PULLUP )
gpio.write( statusLedPin, gpio.LOW )

buttonSwicthAP_STA_Setup()
buttonSwicthAP_STA_Setup = nil

collectgarbage( "setpause", 0 )
collectgarbage( "setstepmul", 10000 )

-- function dumpGlobalObjects()
-- 	-- globals.lua
--     -- show all global variables

--     local seen={}

--     local function tablelength(T)
-- 		local count = 0
-- 		for _ in pairs(T) do count = count + 1 end
-- 		return count
-- 	end

--     local function dump(t,i)
--         seen[t]=true
--         local s={}
--         local n=0
--         for k in pairs(t) do
--             n=n+1 s[n]=k
--         end
--         table.sort(s)
--         for k,v in ipairs(s) do
--         	if type(t[v])=="table" and not seen[t[v]] then
--                 dump(v,i.."\t")
-- 				print( i,v, type(t[v]), tablelength( t[v] ) )
-- 			else
-- 				print( i, v, type( t[v] ) )
--             end
--         end
--     end

--     dump(_G,"")
-- end

if gpio.LOW == gpio.read( buttonAP_Station ) then -- fixed button pressed on label "1"
-- if gpio.HIGH == gpio.read( buttonAP_Station ) then -- fixed button pressed on label "1"
	print("start as a station")
	dofile( "httpsender.lc" )
	dofile( "httpfilesender.lc" )
	dofile( "httpsend.lc" )
	dofile( "espgetstatehtml.lc" )
	dofile( "schedulermanager.lc" )
	gSchedulerManager = SchedulerManager.create()
	gSchedulerManager:load()
	gpio.write( controlPin, controlStateReadFile() )
	connectAsAStation()
	connectAsAStation = nil
	
else -- fixed button pressed on label "0"
	print("start as a setup")
	-- createEnduserSetupHtml()
	endUserSetupStart()
	endUserSetupStart = nil
end

timerLedStart()
timerLedStart = nil