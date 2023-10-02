var matrix = [[0,0,0,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,1,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,1,0,0,0,0,0,1,0,0,0,0,0],[0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,2,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,3,0,0,1,0,2,0,0,4,0,3,0,1,3,0,4,0,0,3,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,1,0,0,0,0,2,0,0,2,0,0,0,0],[0,0,0,2,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,1,0,0,0,0,2,0,0,2,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,2,0,0,2,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0]]
var packages = [{
"name": " reservations.apis", "color": " #3182bd"
}
,{
"name": " authentication.util", "color": " #6baed6"
}
,{
"name": " users.api", "color": " #9ecae1"
}
,{
"name": " users.entities", "color": " #c6dbef"
}
,{
"name": " authentication.filters", "color": " #e6550d"
}
,{
"name": " authentication.services", "color": " #fd8d3c"
}
,{
"name": " authentication.configure", "color": " #fdae6b"
}
,{
"name": " reservations.middleware", "color": " #fdd0a2"
}
,{
"name": " reservations.validators.team", "color": " #31a354"
}
,{
"name": " authentication", "color": " #74c476"
}
,{
"name": " reservations.services", "color": " #a1d99b"
}
,{
"name": " reservations.validators.lesson", "color": " #c7e9c0"
}
,{
"name": " users.services", "color": " #756bb1"
}
,{
"name": " reservations.validators.facility", "color": " #9e9ac8"
}
,{
"name": " users.repositories", "color": " #bcbddc"
}
,{
"name": " authentication.repositories", "color": " #dadaeb"
}
,{
"name": " reservations.repositories", "color": " #636363"
}
,{
"name": " authentication.clients", "color": " #969696"
}
,{
"name": " reservations.clients", "color": " #bdbdbd"
}
,{
"name": " authentication.models", "color": " #d9d9d9"
}
,{
"name": " reservations.validators.equipment", "color": " #3182bd"
}
,{
"name": " reservations.validators.time", "color": " #6baed6"
}
,{
"name": " users", "color": " #9ecae1"
}
,{
"name": " reservations.entities", "color": " #c6dbef"
}
,{
"name": " reservations", "color": " #e6550d"
}
,{
"name": " authentication.entities", "color": " #fd8d3c"
}
,{
"name": " reservations.validators", "color": " #fdae6b"
}
,{
"name": " discovery", "color": " #fdd0a2"
}
,{
"name": " authentication.api", "color": " #31a354"
}
,{
"name": " users.clients", "color": " #74c476"
}
,{
"name": " users.middleware", "color": " #a1d99b"
}
];