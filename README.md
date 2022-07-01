# SSB-ASWM_Lite
###### Developed by [xSavior_of_God](https://github.com/xSavior-of-God)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white) ![SQLite](https://img.shields.io/badge/sqlite-%2307405e.svg?style=for-the-badge&logo=sqlite&logoColor=white) [![Discord](https://img.shields.io/badge/Support-%237289DA.svg?style=for-the-badge&logo=discord&logoColor=white)](https://discord.gg/5GqJbRw)

<br/>

[SuperiorSkyBlock](https://github.com/BG-Software-LLC/SuperiorSkyblock2) Addon that allows you to manage the world of the islands through the [Slime](https://github.com/Paul19988/Advanced-Slime-World-Manager) Region Format

---
⚠ **This plugin doesn't allow you to generate a SlimeWorld for each island!**

<br/>

## ⁉ How is it supposed to work and what benefits does it bring me?
World management and loading remains standard, so you have one world (by environment) that contains all islands in Slime Region Format, its goal is to provide server administrators with an easy-to-use tool to load worlds faster and save space.

By using this plugin you will benefit from the following things:
- Considerable ___saving in the dimension of the world___ that manages the islands
- You can store the ___world in a database___ (so it doesn't necessarily have to be in the same container as the server)
- Since the loading of the world does not vary, you will have ___compatibility with all plugins that interact with worlds___

<br/>

## 🛑 Disadvantages and bugs?
- There are no disadvantages *(If you know any please let me know, I will add them to the list😀)*
- *There is likely to be some unknown bug related to some unexpected condition.*
- ~~Currently, the only BUG I found it related to the management of the teleportation positions at the join after restarting the server, briefly, when I restarted the server I found myself in the default world at the same coordinates as the world I was in...~~
  ***I fixed the bug, so you have nothing to worry about!***

<br/>

## 📌 Installation
1. [Set up the server to support ASWM](https://github.com/Paul19988/Advanced-Slime-World-Manager/blob/develop/.docs/usage/install.md)
2. Make sure the Default world is classic (otherwise inventories may not be saved!) [To be verified!]
3. Install [SuperiorSkyBlock](https://bg-software.com/superiorskyblock/) plugin and stop the server
4. Delete the world folder called "SuperiorWorld" (and also if exist "SuperiorWorld_nether" and "SuperiorWorld_the_end")
5. Edit SuperiorSkyBlock config, set "SuperiorWorld" value to default world name
    <details>
    <summary> Click here to expand </summary>

    ```YAML
    # All settings related to the spawn island.
    spawn:
      # The location of the island. Players will be teleported to this
      # location in many events, such as disbanding islands & getting expelled from one.
      location: SuperiorWorld, 0, 100, 0, 0, 0
    ```
    </details>
6. Install the SSB-ASWM_Lite plugin and stop the server
7. Edit SSB-ASWM_Lite config, set the "data-source" to the on the one you want to use (make sure you have configured it in the SWM-Plugin config before using it!)
    <details>
    <summary> Click here to expand </summary>

    ```YAML
    # Data Source valid options
    # (Source https://github.com/Paul19988/Advanced-Slime-World-Manager/blob/develop/.docs/config/configure-world.md#config-options)
    # file, mysql, mongodb
    data-source: "file"
    ```
    </details>
    ⚠ If you change the storage from "file" to another, be sure to delete the worlds inside worlds.yml of "SlimeWorldManager" plugin folder before restarting the server
8. Now you can restart the server and play with your friends

   ⚠ If the **server Crashes the first time**, don't worry, try restarting it again (**The second crash is not expected!**).

<br/>

## 🔗 Credits
<details>
<summary> <b><a href="https://github.com/OmerBenGera">Ome_R / OmerBenGera</a></b> </summary>
SuperiorSkyBlock2, 
CommentedConfiguration.java, 
Island manager code logic
</details>
<details>
<summary> <b><a href="https://github.com/Paul19988">Paul19988</a></b> </summary>
Advanced-Slime-World-Manager
</details>

<br/>

## ⏳ TODO
- [ ] [**Preparation of The_End World for the generation of the Ender Dragon via SSB methods**](https://github.com/BG-Software-LLC/SuperiorSkyblock2/blob/7664dde301e1d5821963ddee60aa8debc3effda4/src/main/java/com/bgsoftware/superiorskyblock/external/worlds/WorldsProvider_Default.java#L42)

<br/>
<br/>
<br/>

## 🚀 Support

[![support image](https://www.heroxwar.com/discordLogo.png)](https://discord.gg/5GqJbRw)

**[https://discord.gg/5GqJbRw](https://discord.gg/5GqJbRw)**