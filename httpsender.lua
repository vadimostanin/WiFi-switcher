HttpSender = {}
HttpSender.__index = HttpSender

function HttpSender.create( socket )
--print( "HttpSender.create enter" )
    local senderobj = {}
    setmetatable( senderobj, HttpSender )
    senderobj.dataPos = 0
    senderobj.string_data = ""
    senderobj.string_data_count = 0
    senderobj.socket = socket
    senderobj.onsent = nil
    senderobj.isclose = false
--print( "HttpSender.create leave" )
    return senderobj
end

function HttpSender:setOnSent( callback )
    self.onsent = callback
end

function HttpSender:send( string_data, isclose )
    -- print( "HttpSender:send() enter isclose=" .. tostring( isclose ) )
    self.dataPos = 0
    self.string_data = string_data
    self.string_data_count = #string_data
    self.isclose = isclose
    self.socket:on( "sent", 
        function( socket )
                -- print( "socket_send_callback enter" )
                local chunk_size = 600;
                local sentfinished = false
                -- print( "dataPos=" .. self.dataPos .. ", string_data_count=" .. self.string_data_count )
                if self.dataPos < self.string_data_count then
                    local partial_data = string.sub( self.string_data, self.dataPos, self.dataPos + chunk_size );
                    local partial_data_size = #partial_data
                    -- print( "dataPos=" .. self.dataPos .. ", partial_data_size=" .. partial_data_size .. ", chunk_size=" .. chunk_size )
                    if nil ~= partial_data then
                        self.dataPos = self.dataPos + partial_data_size;
                        -- print( "[" .. self.dataPos .. " bytes sent]" );
                        socket:send( partial_data );

                        partial_data = nil
                        partial_data_size = nil
                    end
                else
                    sentfinished = true
                    if true == self.isclose then
                        socket:close()
                        -- print( "close socket" )
                        self.socket = nil
                        self.onsent = nil
                    end
                    self.isclose = nil
                    self.dataPos = nil
                    self.string_data = nil
                    self.string_data_count = nil
                    collectgarbage();
                end
                -- print( "socket_send_callback leave" )
                if true == sentfinished then
                    if nil ~= self.onsent then
                        -- tmr.alarm( 0, 100, tmr.ALARM_SINGLE, 
                        --         function()
                        --             print( "call onsent from socket callback \"sent\"" )
                        --             self.onsent()
                        --         end)
                        self.onsent()
                        collectgarbage();
                    end
                end
           end
        )
             
    local chunk_size = 600;
--    print( "string_data_count=" .. self.string_data_count )
    if self.dataPos < self.string_data_count then
        partial_data = string.sub( self.string_data, self.dataPos, self.dataPos + chunk_size );
        partial_data_size = #partial_data
--        print( "dataPos=" .. dataPos .. ", partial_data_size=" .. partial_data_size .. ", chunk_size=" .. chunk_size )
        if nil ~= partial_data then
            self.dataPos = self.dataPos + partial_data_size;
            -- print( "[" .. self.dataPos .. " bytes sent]" );
            self.socket:send( partial_data );

            partial_data = nil
            partial_data_size = nil
        end
    else
        if true == self.isclose then
            self.socket:close()
            -- print( "close socket" )
            self.socket = nil
            self.onsent = nil
        end
        self.isclose = nil
        self.dataPos = nil
        self.string_data = nil
        self.string_data_count = nil
        collectgarbage();
    end
   -- print( "HttpSender:send() leave" )
end