# NoammExtras

A Fabric client addon for NoammAddons that adds party-chat utilities.

## Included commands
- !opsec: reports OS, Linux distro when available, current resolution, and estimated region
- !femboy: sends a randomized femboy percentage for the sender

## Layout
- src/main/kotlin/com/noamm9/noammextras contains the mod entrypoint and command logic
- src/main/resources/fabric.mod.json defines the mod metadata for Fabric

## Notes
- NoammAddons 1.3.3 does not expose a public clickgui category-registration API, so this addon currently provides its features through party chat commands.
- Party command replies are sent through NoammAddons' paced party-message queue.

## GitHub build
Push a commit or run **Build NoammExtras** from the repository's Actions tab. Download the `NoammExtras` artifact from the completed workflow run, unzip it, and place the included `NoammExtras-<version>.jar` in your Fabric `mods` folder.
