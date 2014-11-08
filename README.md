# Log reader

Read log files right in your web browser.

Just for developpement, DO NOT USE IN PRODUCTION environment or use carefully restricted unix permissions.

## Planned features
- Client-side transformations: add colors, split/join lines, remove noisy/useless data...
- Client-side filter (as in search box)
- File rotation handling (how to present it ?)
- Autocomplete when entering the name of a file (does not show rotated files that will never grow again)
- Maybe view multiple files at the same time
- Handle usage in split-screen mode

## Questions
- How many log files do one need to monitor ? When I develop a php app I usually need Apache error log and the application own log. And maybe the access log. I can't really think of a situation where I need more than 3 files open at the same time.
- When to destroy old lines ? We don't want to steal too much memory from the browser
- How to write the transformers ? Errors logged php by always look the same so we can include a pre-built filter. But most applications have their own log format so it must be easy for someone to write a new transformer without touching the code of the the clojure app and recompiling everything.
- Is there a nice monospace font available we can use ? We may have to pick one for each operating system
