# Towncraft

[![Build](https://github.com/Webhead1104/Towncraft/actions/workflows/qodana_code_quality.yml/badge.svg)](https://github.com/Webhead1104/Towncraft/actions/workflows/qodana_code_quality.yml)

Towncraft is a remake of Township for PaperMC. Place buildings, grow crops, produce resources, and fulfill train orders.

Based on the Township mobile game by Playrix (fan project, non‑commercial).

## Current Status & Roadmap

- Content available: up to level 10.
- Roadmap: new features and content will be released in 10‑level increments (e.g., 1–10 → 11–20 → 21–30 → 31–40, etc.).

## Features

- Production chains: animals, factories, and production recipes
- Train delivery system with timed orders
- Storage and inventory management
- Farm plots and town expansion mechanics
- Community buildings, housing, and population system
- Player‑friendly GUI menus

## How It Works

Navigate your town through inventory menus using `/towncraft`. Your world consists of an 8×8 grid of sections, each
containing 54 buildable tiles. Use arrow controls to move between sections. Buildings occupy multiple tiles based on
their size (e.g., barn = 3×3).

## Requirements

- PaperMC
- Java 17+

## Installation

1) Download the Towncraft plugin JAR from Releases.
2) Place it into your server's `plugins/` folder.
3) Start or restart the server.
4) Optional: choose your storage backend in `plugins/Towncraft/config.yml`.

Quick start in‑game: use `/towncraft` to open the main menu.

## Configuration

Storage backends (pick what fits your server):

- Local files (default, zero setup)
- MySQL (good for networks)
- MongoDB (alternative database)

Player data saves automatically.

## Commands

- `/towncraft` — opens the main Towncraft menu for the player.

Permissions: defaults are intended for general use. Fine‑grained permissions may be added as the project evolves.

## Contributing

Contributions are very welcome!

- File bugs and feature requests in Issues.
- Submit PRs for fixes or features — especially for upcoming 10‑level content increments.
- If proposing a larger change, please open an issue first to discuss design.

## Attribution & Disclaimer

- Game mechanics are recreated from the Township Wiki (CC‑BY‑SA). This project is not affiliated with Playrix
  Entertainment.
- All plugin code is written from scratch for Minecraft servers.

## Support

- Questions or ideas? Open an issue.
- Found a bug? Include server version, plugin version, logs, and steps to reproduce.