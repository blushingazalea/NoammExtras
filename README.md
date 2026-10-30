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
Push a version tag such as `v1.0.1` to build and publish a GitHub Release. Download the `NoammExtras-<version>.jar` asset directly from the repository's **Releases** page and place it in your Fabric `mods` folder. Regular branch builds run in Actions but do not publish downloadable artifacts.
