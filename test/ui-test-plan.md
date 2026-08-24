# UI Test Plan

Scripted console sessions for the `test-ui` skill. Each test case below is
run as its own fresh session: every `input` line is fed to the program in
order, and the text it prints for that command (the content between the two
divider lines) is checked against the matching `expected` block.

The very first thing the program prints -- the startup banner and greeting
-- is shown in the console transcript but is not itself checked, since it
isn't a response to any command.

The program persists tasks in `data/samantha.txt`, relative to its working
directory. Run each test case with an isolated working directory (or remove
that directory's `data/samantha.txt` before starting it) so saved tasks from
one case do not affect another case.

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

## Test 4: Mark and unmark the middle task of several

**Aim:** `mark <n>` and `unmark <n>` flip the checkbox for task `n`
specifically -- not just for the first or last task. The list deliberately
holds three tasks and the test targets the middle one, so an off-by-one in
the index arithmetic, or a bug that always touches `tasks.get(0)`, shows up
as the wrong line changing. The surrounding tasks must stay untouched, which
is why `list` is checked in full after each flip.

```input
todo read book
```
```expected
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
```

```input
todo return book
```
```expected
Got it. I've added this task:
  [T][ ] return book
Now you have 2 tasks in the list.
```

```input
todo join club
```
```expected
Got it. I've added this task:
  [T][ ] join club
Now you have 3 tasks in the list.
```

```input
mark 2
```
```expected
Nice! I've marked this task as done:
  [T][X] return book
```

```input
list
```
```expected
Here are the tasks in your list:
1. [T][ ] read book
2. [T][X] return book
3. [T][ ] join club
```

```input
unmark 2
```
```expected
OK, I've marked this task as not done yet:
  [T][ ] return book
```

```input
list
```
```expected
Here are the tasks in your list:
1. [T][ ] read book
2. [T][ ] return book
3. [T][ ] join club
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
reported with an error message instead of crashing or silently doing
nothing, and neither one adds anything to the task list.

```input
todo
```
```expected
The description of a todo cannot be empty.
```

```input
blah
```
```expected
It seems that you entered a wrong command.
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
an event missing `/to` are all rejected with a specific error message
and don't add anything to the list -- and, importantly, none of them corrupt
internal state: a valid `deadline` right after still gets added as task #1,
not #4.

```input
deadline return book
```
```expected
You forgot to include /by for this deadline.
```

```input
deadline /by Sunday
```
```expected
The description of a deadline cannot be empty.
```

```input
event meeting /from Mon
```
```expected
You forgot to include /from and /to for this event.
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
You forgot to mention the id of the task
```

```input
mark abc
```
```expected
You need to enter a number for the task id.
```

```input
mark 5
```
```expected
You entered a task number that does not exist.
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

## Test 8: Delete a task and confirm the rest renumber

**Aim:** `delete <n>` removes task `n`, reports it with the updated count,
and the tasks after it shift up to fill the gap. The final `mark 2` is the
real point of this test: after deleting task 2 of 3, the old task 3 must now
answer to index 2. A delete that removed the right task but left the list
indexed wrongly would pass every check except that last one.

```input
todo read book
```
```expected
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
```

```input
deadline return book /by June 6th
```
```expected
Got it. I've added this task:
  [D][ ] return book (by: June 6th)
Now you have 2 tasks in the list.
```

```input
event project meeting /from Aug 6th 2pm /to 4pm
```
```expected
Got it. I've added this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 3 tasks in the list.
```

```input
delete 2
```
```expected
Noted. I've removed this task:
  [D][ ] return book (by: June 6th)
Now you have 2 tasks in the list.
```

```input
list
```
```expected
Here are the tasks in your list:
1. [T][ ] read book
2. [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
```

```input
mark 2
```
```expected
Nice! I've marked this task as done:
  [E][X] project meeting (from: Aug 6th 2pm to: 4pm)
```

```input
bye
```
```expected
Bye. Let's talk next time!
```

## Test 9: Reject bad delete input, then confirm the list is intact

**Aim:** `delete` with a missing, non-numeric, out-of-range, or over-supplied
argument is rejected instead of crashing or deleting the wrong thing. The
`delete 1 2` step covers the "too many parameters" branch, which no other
test reaches. The closing `list` proves none of the four rejected commands
removed anything.

```input
todo read book
```
```expected
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
```

```input
delete
```
```expected
You forgot to mention the id of the task
```

```input
delete abc
```
```expected
You need to enter a number for the task id.
```

```input
delete 5
```
```expected
You entered a task number that does not exist.
```

```input
delete 1 2
```
```expected
You entered too many parameters for this operation
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
