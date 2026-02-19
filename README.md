# Ace

Ace is a desktop task manager for quickly managing `todo`, `deadline`, and `event` tasks through a chat-style interface.

## Why Ace

- Fast keyboard-driven task entry
- Supports deadlines and events
- Friendly command aliases (`ls`, `t`, `rm`, `mk`, `um`)
- Saves tasks to local storage automatically

## Features

| Action | Command | Alias | Example |
|---|---|---|---|
| Add todo | `todo <description>` | `t <description>` | `todo review CS2103 notes` |
| Add deadline | `deadline <description> /by <yyyy-MM-dd>` | - | `deadline submit iP /by 2026-03-01` |
| Add event | `event <description> /from <start> /to <end>` | - | `event project meeting /from 2pm /to 4pm` |
| List tasks | `list` | `ls` | `ls` |
| Mark done | `mark <task number>` | `mk <task number>` | `mk 2` |
| Unmark task | `unmark <task number>` | `um <task number>` | `um 2` |
| Delete task | `delete <task number>` | `rm <task number>` | `rm 1` |
| Find tasks | `find <keyword>` | - | `find project` |
| Exit app | `bye` | - | `bye` |

## Example Session

```text
todo read chapter 6
deadline submit iP /by 2026-03-01
event project meeting /from 2pm /to 4pm
list
mark 2
find project
delete 1
bye

## Acknowledgements

- This project used AI assistance (ChatGPT) to brainstorm parser edge cases, improve input-validation ideas, and suggest regression test scenarios.