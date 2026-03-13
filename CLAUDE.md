# Interview App - Project Notes

> **IMPORTANT**: Keep this file updated when making significant changes to the codebase. This file serves as persistent memory between Claude Code sessions.

## Overview
Technical interview management platform. Create/manage questions and coding tasks, define question configs, conduct live interviews with scoring and auto-save, generate AI evaluation prompts.

## Key Architecture

### Entities

#### Question
- `id: UUID`, `name: String`, `tags: String` (comma-separated), `difficulty: Integer` (1-5)
- `questionDetails: String` (max 5000 MEDIUMTEXT), `answerDetails: String` (max 5000)
- `maxPoints: Double`, `modificationDate`, `deleted: Boolean`
- `history: Set<HistoryQuestion>`

#### CodingTask
- `id: UUID`, `name: String`, `initialCode: String`, `exampleCode: String`
- `exampleCodeDetails: String`, `questions: String`
- `modificationDate`, `deleted: Boolean`, `history: Set<HistoryCodingTask>`

#### QuestionConfig
- `id: UUID`, `name: String` (unique per owner)
- `difficulty1Percent` … `difficulty5Percent: Integer` — distribution by difficulty
- `codingTasksAmount: Integer`

#### InterviewSession
- `id: UUID`, `candidateName: String`, `configName: String`, `totalQuestions: Integer`
- `mainTags: String`, `secondaryTags: String`
- `notes: String` (max 500KB LONGTEXT), `planTemplate: String`, `feedback: String` (max 500KB)
- `status: String` — IN_PROGRESS or COMPLETED
- `sessionQuestions: List<InterviewSessionQuestion>`
- `sessionCodingTasks: List<InterviewSessionCodingTask>`

#### InterviewSessionQuestion
- `id: UUID`, `questionNumber: Integer`, `score: Double`
- `notes: String` (max 5000), `answerStatus: String` — NOT_ASKED, CORRECT, PARTIAL, INCORRECT
- `session: ManyToOne`, `question: ManyToOne`

### Services

#### InterviewSessionService
- `saveWithHistory()` — saves session + questions; pre-loads into 1st-level cache to avoid Hibernate lazy proxy issues
- `autoSave()` — periodic save every 2 min; checks DB status before saving (ghost scheduler protection)

### Views (Route prefix: `interview-app/`)

| Route | View | Purpose |
|-------|------|---------|
| `home` | AbstractInterviewHomeView | Welcome + quick access |
| `interview-process` | AbstractStartInterviewView | Start new interview |
| `interview-questions` | AbstractInterviewQuestionsView | Browse/manage questions |
| `coding-tasks` | AbstractCodingTaskView | Browse/manage coding tasks |
| `interview-plan` | AbstractInterviewPlanView | Create interview scripts |
| `sessions` | AbstractInterviewSessionListView | List all sessions |
| `question-config` | AbstractQuestionConfigView | Manage configs |
| `import-export` | AbstractImportExportView | Excel import/export |
| `interview-session/:id` | AbstractInterviewSessionView | Conduct live interview |

#### AbstractInterviewSessionView (Live Interview Conductor)
- Question cards with answer status buttons (NOT_ASKED/CORRECT/PARTIAL/INCORRECT)
- Click to show/hide expected answers (one at a time)
- Per-question score fields, notes
- Plan template variable substitution: `{questions}`, `{codingTasks}`, `{candidateName}`, `{configName}`, `{mainTags}`, `{secondaryTags}`, `{totalQuestions}`
- AI prompt generation from interview data
- Session completion → read-only + feedback section

## Configuration
- `src/main/resources/autoconfig/Question.yml` — question form fields
- `src/main/resources/autoconfig/QuestionConfig.yml` — config form fields

## Important Notes
1. Auto-save runs every 2 minutes for IN_PROGRESS sessions only
2. Ghost scheduler protection: checks DB status before writing
3. `saveWithHistory()` pre-loads session to avoid Hibernate proxy issues during history serialization
4. Soft deletes via `deleted` flag; IN_PROGRESS → COMPLETED lifecycle
5. Tags are comma-separated strings with case-insensitive matching
6. Excel import/export via `ExcelIEEntity<UUID>`
7. Multi-tenancy via `BervanOwnedBaseEntity`
