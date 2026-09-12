# Samantha

Samantha is a personal assistant for tasks and notes. Type a command, or click **?** in the window if you would rather pick from a guide.

![The Samantha window](Ui.png)

You can also run Samantha from a terminal. The commands are the same.

Dates use `d/M/yyyy` or `d-M-yyyy`. Times use `HHmm`, for example `1800`.

## Add a task

```
todo read chapter 5
deadline submit report /by 12/9/2026 1800
event project meeting /from 12/9/2026 1400 /to 12/9/2026 1600
```

An event must end after it starts. A deadline may omit the time.

## Look at your list

```
list
list 12/9/2026
find report
```

`list` with a date shows deadlines and events on that day. `find` searches tasks and notes.

## Update a task

Use the number shown by `list`:

```
mark 1
unmark 1
delete 1
undo
```

`undo` reverses the last change to a task or note.

## Keep a note

```
note buy oat milk
notes
edit-note 1 buy oat milk and bread
delete-note 1
```

## Get help and leave

```
help
bye
```

In the window, `help` or **?** opens a command guide. Press `c` there to copy an example.
