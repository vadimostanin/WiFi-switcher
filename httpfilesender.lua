HttpFileSender = {}
HttpFileSender.__index = HttpFileSender

function HttpFileSender.create( socket, filepath )
    local senderobj = {}             -- our new object
    setmetatable( senderobj, HttpFileSender )  -- make Account handle lookup
    senderobj.dataPos = 0      -- initialize our object
    senderobj.filepath = filepath      -- initialize our object
    senderobj.socket = socket      -- initialize our object
    
    return senderobj
end

function HttpFileSender:send( header )
	self.dataPos = 0      -- initialize our object
--    print( "HttpSender:send() enter" )
    self.socket:on( "sent", 
        function( socket )
--              print( "socket_send_callback enter" )
                local chunk_size = 512;
                if file.open( self.filepath, r ) then
                    file.seek( "set", self.dataPos );
                    local partial_data = file.read( chunk_size );
                    file.close();
                    if nil ~= partial_data then
                        self.dataPos = self.dataPos + #partial_data;
                        -- print( "[" .. self.dataPos .. " bytes sent]" );
                        socket:send( partial_data );
                        collectgarbage('collect');
                    else
                        socket:close()
                        self.socket = nil
                        self.dataPos = nil
                        self.filepath = nil
                        collectgarbage('collect');
                    end
                else
                    -- print( "[Error opening file" .. self.filepath.."]" );
                end
--             print( "socket_send_callback leave" )
           end
        )
             
    self.socket:send( header );
    collectgarbage('collect');
--    print( "HttpSender:send() leave" )
end
