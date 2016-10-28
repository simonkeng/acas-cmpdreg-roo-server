prefix='http'
host=$(hostname --fqdn)
#host='host5.labsynch.com'
port='8080'
path='cmpdreg'

fullpath=$prefix://$host:$port/$path/applicationsettings
echo $fullpath
propNames=('prefix' 'host' 'port' 'path' 'file_path' 'lot_path')
propValues=("$prefix" "$host" "$port" "$path" 'MultipleFilePicker' '#lot')
recordedDate=$(($(date +'%s * 1000 + %-N / 1000000')))

for (( i = 0 ; i < ${#propNames[@]} ; i++ ))
do
echo "	Setting ${propNames[$i]} to ${propValues[$i]}"
command="curl -i -X POST -H \"Content-Type: application/json\" -H \"Accept: application/json\" -d  \"{propName: '${propNames[$i]}', propValue: '${propValues[$i]}', recordedDate: ${recordedDate}}\" $fullpath"
echo $command
eval $command
done

curl -i -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d 'compoundAdmin' http://$host:$port/$path/api/v1/createAllJChemTables

curl -i -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d 'compoundAdmin' http://$host:$port/$path/api/v1/createPreDefCorpNames


curl -i -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d '[
{ "code": "solid", "name": "solid"},
{ "code": "liquid", "name": "liquid"},
{ "code": "glass", "name": "glass"},
{ "code": "oil", "name": "oil"},
{ "code": "resin", "name": "resin"},
{ "code": "solution", "name": "solution"}
]' http://$host:$port/$path/api/v1/physicalStates/jsonArray

curl -i -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d '[
{"code": "scalemic", "name": "Scalemic"},
{"code": "racemic", "name": "Racemic"},
{"code": "achiral", "name": "Achiral"},
{"code": "see_comments", "name": "See Comments"},
{"code": "unknown", "name": "Unknown"}
]' http://$host:$port/$path/api/v1/stereoCategories/jsonArray

curl -i -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d '[
{"name":"Deuterium labeled","abbrev":"2H","massChange":1},
{"name":"Tritium labeled","abbrev":"3H","massChange":2},
{"name":"13C labeled","abbrev":"13C","massChange":1},
{"name":"14C labeled","abbrev":"14C","massChange":2}
]' http://$host:$port/$path/api/v1/isotopes/jsonArray

curl -i -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d '[
{"code": "=", "name": "="},
{"code": "<", "name": "<"},
{"code": ">", "name": ">"}
]' http://$host:$port/$path/api/v1/operators/jsonArray

curl -i -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d '[
{"code": "Project 1", "name": "Project 1"}
]' http://$host:$port/$path/api/v1/projects/jsonArray

curl -i -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d '[
{"code": "mg", "name": "mg"},
{"code": "g", "name": "g"},
{"code": "kg", "name": "kg"},
{"code": "mL", "name": "mL"}
]' http://$host:$port/$path/api/v1/units/jsonArray

curl -i -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d '[
{"code": "aadmin", "name": "Adam Admin","isChemist":false,"isAdmin":true}
]' http://$host:$port/$path/api/v1/scientists/jsonArray

curl -i -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d '[
{"code":"HPLC","name":"HPLC","version":0},
{"code":"LCMS","name":"LCMS","version":0},
{"code":"NMR","name":"NMR","version":0}
]' http://$host:$port/$path/api/v1/fileTypes/jsonArray