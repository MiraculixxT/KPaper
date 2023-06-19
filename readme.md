# KPaper

KPaper is a reimagination from [KSpigot](https://github.com/jakobkmar/KSpigot) focussing on full version compatibility, 
less specific and unsafe features and with a focus on the [Adventure](https://docs.advntr.dev/getting-started.html) library inbuilt in PaperMC.

KPaper also implements useful utilities with a higher focus on adjustability.
If you search for a cross-platform solution to 
the cost of version independence, I highly recommend taking a look at Silk-Paper.

## Dependency

KPaper is available on Maven Central.

Gradle Kts:

```kt
dependencies {
    implementation("de.miraculixx:kpaper:1.1.0")
}
```
And don't forget to add it to your libraries inside the ``plugin.yml``
````yml
[...]
libraries:
  - "de.miraculixx:kpaper:1.1.0"
````

## Changes - KPaper to KSpigot 
All feature changes are split into a title shortly describing the change and more detailed information about it inside the spoiler.
### • New features 
<details>
  <summary>Easy Text Components</summary>
Adventure's text component system is very close to Minecraft's original message system covering all features to the cost of 
becoming very messy quickly. 

KPaper now features a bunch of extension functions and entry functions to work with text components without writing a lot more code.

Example: "Click here to open the url" where 'here' should open a link and is highlighted by color and underlined.
```kotlin
//Without KPaper
Component.text("Click ")
    .color(NamedTextColor.GRAY)
    .append(
        Component.text("here")
            .color(NamedTextColor.BLUE)
            .decoration(TextDecoration.UNDERLINED, TextDecoration.State.TRUE)
            .clickEvent(ClickEvent.openUrl("https://modrinth.com"))
    )
    .append(
        Component.text(" to open the url!")
            .color(NamedTextColor.GRAY)
    )

//With KPaper
cmp("Click ") + 
        cmp("here", KColors.BLUE, underlined = true).addUrl("https://modrinth.com") + 
        cmp(" to open the url!")
```
As you can see in this example, we save a lot of bloat by adding all values directly to the component function. 
Additionally, the color GRAY is set as the default color to prevent entering it every time again. 
Defaults can be changed at any time with the KPaperConfiguration object
</details>
<details>
    <summary>Audience Extensions</summary>
Audiences (by Adventure) represent every entity and CommandSender (like console). 
An Audience object can contain multiple entities or CommandSender to simply bulk actions.
With KPaper you can simply add multiple audiences together by using the + operator.
In addition KPaper adds some utilities like easy titles packet with kotlin durations and more.
</details>
<details>
    <summary>Custom Head Library</summary>
When working with GUIs/Inventories you often use some kind of heads to display certain actions or just for a better look.
Instead of looking them up everytime, KPaper now implements a bunch of frequently used heads with a fancy preview. 
If you have some general heads, simply create a new PR!

![Head Preview](https://i.imgur.com/yO2qt2y.png)
</details>
<details>
    <summary>Extend / Improve Awaits</summary>
Awaiting any type of input for a player with your own callbacks.

- Chat Message - Return the next chat message, now with onTimeout and opt in formatting options
- Book Input - Return the next chat input, now with onTimeout
</details>
<details>
    <summary>Scoreboard API</summary>
Create and update player specific scoreboards easily without the trouble to manage all scoreboards!

You can add new lines, edit existing lines, remove lines out of order, hide/show the scoreboard and remove it completely.
</details>

### • Removed Features
<details>
    <summary>IP Blocker</summary>
Alls of the provided services are paid closed source companies. 
There are free limited versions provided with a request limit or something else, but I don't think this fits into KPaper at all.
Additionally, the feature to detect VPNs, proxies or similar should be implemented by an exact plugin targeting this.
</details>
<details>
    <summary>Game Phases</summary>
With the game phase api you could add simple mini-game states. 
Again, this feels like too specific to be in KPaper and too pure for actual mini-game servers.
</details>
<details>
    <summary>Brigadier Commands</summary>
Proably the most used feature that will be cut out of KPaper.
The simple reason for this is the version independence. 
The current implementation does not allow the use in multiple versions and can break even in a single minor Minecraft update.

Nonetheless, I highly recommend staying at brigadier with a library specialized to support it in all versions.
The library [CommandAPI](https://commandapi.jorel.dev/8.8.0/kotlindsl.html) by Jorel follows a similar syntax and supports kotlin too.
To learn more about it, visit their [documentation](https://commandapi.jorel.dev/8.8.0/kotlinintro.html)
</details>
<details>
    <summary>Structure API</summary>
In favor of the WorldEdit or FAWE API. Using structures in general is a rare case and does not fit into KPaper
</details>
