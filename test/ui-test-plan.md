# UI Test Plan

Scripted console sessions for the `test-ui` skill. Each test case below is
run as its own fresh session: every `input` line is fed to the program in
order, and the text it prints for that command (the content between the two
divider lines) is checked against the matching `expected` block.

The very first thing the program prints -- the startup banner and greeting
-- is shown in the console transcript but is not itself checked, since it
isn't a response to any command.

## Format for new test cases

```
## Test <n>: <short name>

**Aim:** <what behaviour this checks, and why it matters>

\`\`\`input
<one command>
\`\`\`
\`\`\`expected
<exactly what should appear between the divider lines for that command>
\`\`\`
```

Repeat the `input`/`expected` pair for each command in the session, in
order. Every test case should end with a `bye` step so the process
terminates cleanly instead of crashing on end-of-input.

## Test 1: Greet and exit

**Aim:** The program greets the user automatically on startup (checked via
the transcript, not asserted here) and prints a farewell message then exits
cleanly when the very first command is `bye`.

```input
bye
```
```expected
Bye. Let's talk next time!
```

## Test 2: Add a todo and list it

**Aim:** Adding a todo confirms the task with its `[T]` marker and an
unchecked box, updates the task count, and the same line then shows up
under `list`.

```input
todo borrow book
```
```expected
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
```

```input
list
```
```expected
Here are the tasks in your list:
1. [T][ ] borrow book
```

```input
bye
```
```expected
Bye. Let's talk next time!
```

## Test 3: Add a deadline and an event, then list

**Aim:** A deadline's `/by` date and an event's `/from`/`/to` range are
parsed out of the command and shown correctly in both the add confirmation
and `list`, and the task count accumulates across different task types.

```input
deadline return book /by Sunday
```
```expected
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 1 tasks in the list.
```

```input
event project meeting /from Mon 2pm /to 4pm
```
```expected
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 2 tasks in the list.
```

```input
list
```
```expected
Here are the tasks in your list:
1. [D][ ] return book (by: Sunday)
2. [E][ ] project meeting (from: Mon 2pm to: 4pm)
```

```input
bye
```
```expected
Bye. Let's talk next time!
```

## Test 4: Mark and unmark a task

**Aim:** `mark <n>` and `unmark <n>` flip the checkbox for task `n`, and the
change is reflected both in the confirmation message and in a later `list`.

```input
todo read book
```
```expected
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
```

```input
mark 1
```
```expected
Nice! I've marked this task as done:
  [T][X] read book
```

```input
list
```
```expected
Here are the tasks in your list:
1. [T][X] read book
```

```input
unmark 1
```
```expected
OK, I've marked this task as not done yet:
  [T][ ] read book
```

```input
list
```
```expected
Here are the tasks in your list:
1. [T][ ] read book
```

```input
bye
```
```expected
Bye. Let's talk next time!
```

## Known gaps not yet covered

These are real behaviours worth testing eventually, but they're deliberately
left out for now because the program doesn't handle them gracefully yet --
error handling is Level-5's job, not this test plan's:

- `mark`/`unmark`/`deadline`/`event` with missing or malformed arguments
  (e.g. `mark abc`, `mark 99`, `deadline no slash here`) currently crash the
  program instead of printing an error message.
- A command word that isn't `todo`/`deadline`/`event`/`mark`/`unmark`/`list`
  (e.g. a typo, or plain text with no command) currently produces no output
  at all instead of an error message.

Add test cases for these once Level-5 gives them well-defined expected
output.
