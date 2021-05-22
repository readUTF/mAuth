# mAuth

mAuth is a open-source staff verification plugin and discord bot for spigot servers.

## Getting Started



To use this plugin you will need a discord bot token, you can read [here](https://www.writebots.com/discord-bot-token/) for help finding your token. Make sure that the bot has the permission to modify/delete other users messages. Once the jar is added into your plugins folder, a config.yml file will be generated, where you are able to specify your discord token. You can also define a mysql or mongodb database in the config.yml generated when the server starts (See Below).

```yml
#DO NOT CHANGE OR REVEAL THIS CODE
gauth-secret: ""
bot:
  #This enables any feature that involves discord syncing.
  enabled: false
  auth: ""
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
location-change:
  #if set to false, verification will take place through the server.
  use-discord: true
2fa:
  #must be DISCORD or GOOGLE
  type: "DISCORD"
sync:
  message:
    - "&eTo sync your account, please follow the following steps:"
    - ""
    - " &61. &eJoin our discord: discord.example.net"
    - " &62. &eEnter the channel #sync-account and send the code &6{code}"
    - " &63. &eSuccess! If everything went right your account is now synced!"
    - ""
```

The next step is to create a channel named "staff-activity" in your server, this is where the bottom will send messages and allow you to authenticate/disable accounts.

## 2FA

Two factor authentication has two modes, 'discord' and 'google, which can be switchen between in the config.yml

### Discord Mode 
On connection, the user will be unable to move or interact with the server. A message will be sent to the users linked discord account (See Discord Sync Section) which when reacted to will authenticate and unfreeze the player.

<img src="https://i.imgur.com/8bxwjfQ.png" width="640" height="339">
<img src="https://i.imgur.com/9Nkuhk4.png">


