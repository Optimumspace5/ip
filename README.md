# Ace

Ace is a desktop task manager that helps you track `todo`, `deadline`, and `event` tasks quickly using typed commands.

## Features

- Add tasks:
  - `todo <description>`
  - `deadline <description> /by <yyyy-MM-dd>`
  - `event <description> /from <start> /to <end>`
- List all tasks: `list` or `ls`
- Mark task as done: `mark <task number>` or `mk <task number>`
- Unmark task: `unmark <task number>` or `um <task number>`
- Delete task: `delete <task number>` or `rm <task number>`
- Find tasks: `find <keyword>`
- Exit app: `bye`

## Example Usage

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