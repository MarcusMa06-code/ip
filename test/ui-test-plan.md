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

## Test 5: Reject an empty todo and an unrecognized command

**Aim:** The two error cases required by the Level-5 spec -- an empty todo
description, and a command word the program doesn't recognize -- are
reported with an `OOPS!!!` message instead of crashing or silently doing
nothing, and neither one adds anything to the task list.

```input
todo
```
```expected
OOPS!!! The description of a todo cannot be empty.
```

```input
blah
```
```expected
OOPS!!! It seems that you entered a wrong command.
```

```input
list
```
```expected
Here are the tasks in your list:
```

```input
bye
```
```expected
Bye. Let's talk next time!
```

## Test 6: Reject malformed deadlines and events, then confirm a valid one still works

**Aim:** A deadline missing `/by`, a deadline with an empty description, and
an event missing `/to` are all rejected with a specific `OOPS!!!` message
and don't add anything to the list -- and, importantly, none of them corrupt
internal state: a valid `deadline` right after still gets added as task #1,
not #4.

```input
deadline return book
```
```expected
OOPS!!! You forgot to include /by for this deadline.
```

```input
deadline /by Sunday
```
```expected
OOPS!!! The description of a deadline cannot be empty.
```

```input
event meeting /from Mon
```
```expected
OOPS!!! You forgot to include /from and /to for this event.
```

```input
deadline return book /by Sunday
```
```expected
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 1 tasks in the list.
```

```input
list
```
```expected
Here are the tasks in your list:
1. [D][ ] return book (by: Sunday)
```

```input
bye
```
```expected
Bye. Let's talk next time!
```

## Test 7: Reject bad mark input, then confirm a valid mark still works

**Aim:** `mark` with no number, a non-numeric number, and a number outside
the task list's range are each rejected with a specific message instead of
crashing, and a valid `mark` afterward still flips the right task.

```input
todo read book
```
```expected
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
```

```input
mark
```
```expected
OOPS!!! You forgot to mention the id of the task
```

```input
mark abc
```
```expected
OOPS!!! You need to enter a number for the task id.
```

```input
mark 5
```
```expected
OOPS!!! You entered a task number that does not exist.
```

```input
mark 1
```
```expected
Nice! I've marked this task as done:
  [T][X] read book
```

```input
bye
```
```expected
Bye. Let's talk next time!
```
