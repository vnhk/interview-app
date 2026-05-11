# interview-app

Technical interview management platform. Create question banks and coding tasks, define interview configurations, conduct live interviews with scoring, and generate AI evaluation prompts.

## Features

- **Question bank**: Questions tagged by topic and difficulty (1–5), with expected answers
- **Coding tasks**: Initial code templates with example solutions
- **Interview configs**: Define difficulty distribution percentages and number of coding tasks
- **Live interview**: Score questions (NOT_ASKED / CORRECT / PARTIAL / INCORRECT), take per-question notes, reveal answers on demand
- **Auto-save**: Every 2 minutes during IN_PROGRESS sessions (ghost scheduler protection)
- **AI prompts**: Generate evaluation prompts from session data
- **Import/Export**: Excel-based bulk import/export

## Key Entities

| Entity | Description |
|--------|-------------|
| `Question` | Question with tags, difficulty, and expected answer |
| `CodingTask` | Coding challenge with initial code and examples |
| `QuestionConfig` | Difficulty distribution + coding task count |
| `InterviewSession` | Full session with candidate name, questions, scores, notes |
| `InterviewSessionQuestion` | Per-question score, status, and notes within a session |

## Routes (`interview-app/`)

| Path | Purpose |
|------|---------|
| `home` | Welcome and quick access |
| `interview-process` | Start a new interview |
| `interview-questions` | Browse and manage questions |
| `coding-tasks` | Browse and manage coding tasks |
| `interview-plan` | Create interview plan templates |
| `sessions` | List all past sessions |
| `question-config` | Manage interview configurations |
| `import-export` | Excel import/export |
| `interview-session/{id}` | Conduct a live interview |

## Plan Template Variables

`{questions}`, `{codingTasks}`, `{candidateName}`, `{configName}`, `{mainTags}`, `{secondaryTags}`, `{totalQuestions}`

## Build

```bash
mvn clean install -DskipTests
```

Part of the `my-tools` multi-module Maven project. Requires `common` to be built first.
