
local httpRequest={}
local function httpAddFileRequest( path, file )
    httpRequest[ path ] = file;
end
function httpGetFileRequest( path )
    return httpRequest[ path ];
end
--httpAddFileRequest( "/", "index.htm" );
httpAddFileRequest( "/espwebhtml.js", "espwebhtml.js" );
httpAddFileRequest( "/scheduler.html", "scheduler.html" );

local getContentType={};
local function httpAddContentType( path, type )
    getContentType[ path ] = type;
end
function httpGetContentType( path )
    return getContentType[ path ];
end
--httpAddContentType( "/", "text/html" );
httpAddContentType( "/espwebhtml.js", "application/javascript" );
httpAddContentType( "/scheduler.html", "text/html" );

function httpsendfull_3( socket, string_data )
    -- print( "string_data_len=" .. #string_data )
    local sender = HttpSender.create( socket )
    sender:send( string_data, true )
--    sender = nil
end
