#Usage command line:
java -jar backup_service.jar [source... target]
java -jar backup_service.jar [-c source... target_file]
java -jar backup_service.jar [-server PORT target_dir]
java -jar backup_service.jar [-client PORT HOSTNAME source_dir]
java -jar backup_service.jar [-h | --help]
java -jar backup_service.jar [--version]

#Configuration file example
Change default conf.json
{
  "type": "Server", // type: Client or Server
  "port": 3333, // Port 
  "dir": "G:/TestFolder/Network_test" // Server: Destination folder || Client: Source folder
}
############################// Windows folder slash / or \\#################################
