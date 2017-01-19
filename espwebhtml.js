function createRadioONOFF( id, name, value, text, checked )
{
var radioObj = createInput( id, name, "radio" )
radioObj.checked = checked;
radioObj.value = value;
radioObj.innerHTML = text;
return radioObj;
}

function createInput( id, name, type )
{
//create input element
var inputObj = document.createElement( "input" );
inputObj.name = name;
inputObj.id = id;
inputObj.type = type;

return inputObj;
}

function clearChilds( id )
{
var myDiv = document.getElementById( id );
while( myDiv.firstChild )
{
myDiv.removeChild( myDiv.firstChild );
}
}

function createSchedulerFormOnes()
{
clearChilds( "schedule_form" );

//Create array of options to be added
var array = ["ON","OFF"];
var myDiv = document.getElementById( "schedule_form" );

var labelDate = document.createElement("label"); 
labelDate.for="schedule_date"; 
labelDate.innerHTML = "<h3>Date:</h3>";
myDiv.appendChild( labelDate );

var scheduleDate = createInput( "schedule_date", "schedule_date", "date" );
myDiv.appendChild( scheduleDate );


var labelTime = document.createElement("label"); 
labelTime.for="schedule_date"; 
labelTime.innerHTML = "<h3>Time:</h3>";
myDiv.appendChild( labelTime );

var scheduleTime = createInput( "schedule_time", "schedule_time", "time" );
myDiv.appendChild( scheduleTime );
}

function createSchedulerFormEveryday()
{
clearChilds( "schedule_form" );
var scheduleTime = createInput( "schedule_time", "schedule_time", "time" );
var myDiv = document.getElementById( "schedule_form" );
var labelTime = document.createElement("label"); 
labelTime.for="schedule_date"; 
labelTime.innerHTML = "<h3>Time:</h3>";
myDiv.appendChild( labelTime );

myDiv.appendChild( scheduleTime );
}