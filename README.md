# mAuth

mAuth is a open-source staff verification plugin and discord bot for spigot servers.

## Getting Started

To use this plugin you will need a discord bot token, you can read [here](https://www.writebots.com/discord-bot-token/) for help finding your token. Make sure that the bot has the permission to modify/delete other users messages 
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

The next step is to create a channel named "staff-activity" in your server, this is where the bottom will send messages and allow you to authenticate/disable accounts.

## Using the plugin

Once you have installed and setup the plugin, any user who connects to the server with the permission `mauth.verify` will be checked to see if their ip-address has changed. If their ip-address has changed, a message will be sent in the #staff-activity channel (Seen below).

<img src="https://i.imgur.com/hZiYUDG.png">

Accounts can also be deactivated by clicking the ❌ button when a connection is flagged or by using the using the discord command `!deactivate <username>` and can be reactivated using `!reactivate <username>`

## Future Features

** Account Linking
** Verification Codes
** Google Authentication
** Password Protection
