# mAuth

mAuth is a open-source staff verification plugin and discord bot for spigot servers.

## Summary




## Getting Started

To use this plugin you will need a discord bot token, you can read [here](https://www.writebots.com/discord-bot-token/) for help finding your token.
You can also define a mysql or mongodb database in the config.yml generated when the server starts (See Below).

```yml
bot:
  auth: "(discord token)"
mysql:
  enabled: false
  host: ""
  port: 3306
  database: ""
  username: ""
  password: ""
mongodb:
  enabled: false
  host: ""
  port: 3306
  database: ""
```
