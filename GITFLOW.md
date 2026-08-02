# Gitflow Branching Strategy

This project follows **Gitflow** branching model with automatic semantic versioning via Maven Git Versioning Extension.

## Branch Structure

### Primary Branches

#### `main` (Production)
- **Purpose**: Production-ready releases only
- **Triggered by**: Release branches (release/*) merging back
- **Version**: Semantic release tags (v1.0.0, v1.1.0)
- **CI/CD**: Build, test, and deploy to production
- **Versioning**: Uses git tags `v*` for version resolution

#### `develop` (Integration)
- **Purpose**: Development and integration branch
- **Triggered by**: Feature branches merging in
- **Version**: Semantic version with `-SNAPSHOT` suffix (e.g., 1.1.0-SNAPSHOT)
- **CI/CD**: Build, test, publish snapshot artifacts
- **Versioning**: Next version from last release + SNAPSHOT

### Supporting Branches

#### `feature/*` (Feature Development)
- **Naming**: `feature/feature-name` or `feature/JIRA-123-feature-name`
- **Branch from**: `develop`
- **Merge back to**: `develop` via Pull Request
- **Version**: `<nextVersion>-<feature-name>-SNAPSHOT`
- **Lifecycle**: Delete after merge
- **Example**: `feature/user-auth`, `feature/PROJ-456-payment-integration`

#### `release/*` (Release Preparation)
- **Naming**: `release/1.0.0` (must match version pattern)
- **Branch from**: `develop`
- **Merge to**: Both `main` (via PR) and back to `develop`
- **Version**: `<release-version>-RC1` (Release Candidate)
- **Lifecycle**: Delete after merge to main
- **Purpose**: Final QA, hotfixes, version bumping
- **Example**: `release/1.0.0`

#### `hotfix/*` (Production Fixes)
- **Naming**: `hotfix/1.0.1` (must match version pattern)
- **Branch from**: `main`
- **Merge to**: Both `main` (via PR) and back to `develop`
- **Version**: `<hotfix-version>` (exact version)
- **Lifecycle**: Delete after merge to main
- **Purpose**: Critical production fixes only
- **Example**: `hotfix/1.0.1`

## Workflow by Scenario

### 1. New Feature Development
```bash
# Create feature branch from develop
git checkout develop
git pull origin develop
git checkout -b feature/PROJ-123-new-feature

# Make changes, commit, and push
git add .
git commit -m "feat: implement new feature"
git push origin feature/PROJ-123-new-feature

# Create Pull Request to develop
# After approval and merge, branch auto-deleted
```

**Version During Development**: `1.1.0-PROJ-123-new-feature-SNAPSHOT`

### 2. Release to Production
```bash
# Step 1: Create release branch from develop
git checkout develop
git pull origin develop
git checkout -b release/1.1.0
git push origin release/1.1.0

# Step 2: QA testing happens here
# Fix any issues: git commit -m "fix: release issue"
# Version is: 1.1.0-RC1

# Step 3: GitHub Actions automatically:
# - Creates release commit when tag is created
# - Increments patch version for next snapshot
# - Updates develop with next snapshot version
```

**Versions During Release**:
- On branch: `1.1.0-RC1` (Release Candidate)
- After tag: `1.1.0` (Release)
- Next develop: `1.1.1-SNAPSHOT`

### 3. Hotfix Production Issue
```bash
# Create hotfix branch from main
git checkout main
git pull origin main
git checkout -b hotfix/1.1.1
git push origin hotfix/1.1.1

# Fix the issue
git add .
git commit -m "fix: critical production issue"
git push origin hotfix/1.1.1

# Create Pull Request to main
# After approval and merge, branch auto-deleted
```

**Version During Hotfix**: `1.1.1`

## Automatic Versioning Rules

### Maven Git Versioning Extension Configuration

| Branch Pattern | Version Format | Example |
|---|---|---|
| `main` | Git tag version | `1.0.0`, `1.1.0` |
| `develop` | Next version + `-SNAPSHOT` | `1.1.0-SNAPSHOT` |
| `feature/NAME` | Next version + `-NAME-SNAPSHOT` | `1.1.0-feature-123-SNAPSHOT` |
| `release/X.Y.Z` | `X.Y.Z-RC1` | `1.1.0-RC1` |
| `hotfix/X.Y.Z` | `X.Y.Z` | `1.1.1` |
| Git tags `vX.Y.Z` | Exact version | `1.0.0` |

## GitHub Workflows

### On Feature Branch Push
- ✅ Unit tests & integration tests
- ✅ Code quality checks
- ✅ Build verification
- ✅ Creates preview artifacts (not published)

### On Pull Request
- ✅ All branch checks run
- ✅ Required approvals before merge
- ✅ Merge commits to target branch

### On Develop Commits
- ✅ Full build & test
- ✅ Publishes SNAPSHOT to GitHub Packages
- ✅ Artifacts tagged with snapshot version

### On Release Branch Commits
- ✅ Full build & test
- ✅ Publishes Release Candidate to GitHub Packages
- ✅ Creates tag when ready (manual trigger recommended)

### On Main Commits (from Release/Hotfix)
- ✅ Full build & test
- ✅ Publishes RELEASE to GitHub Packages
- ✅ Automatically increments next snapshot version
- ✅ Updates develop branch with new snapshot version

## Version Bumping Strategy

### Semantic Versioning (SEMVER): MAJOR.MINOR.PATCH

- **MAJOR**: Breaking changes (1.0.0 → 2.0.0)
- **MINOR**: New features, backwards compatible (1.0.0 → 1.1.0)
- **PATCH**: Bug fixes (1.0.0 → 1.0.1)

### Automatic Bumping Rules

- **develop snapshot**: Minor bump from last release (auto)
- **release branches**: Manual (configure release/X.Y.Z)
- **hotfix branches**: Manual (configure hotfix/X.Y.Z)
- **main**: Uses git tags (e.g., v1.0.0)

### Manual Version Bumping

To bump versions manually during release:

```bash
# In a release or hotfix branch
mvn versions:set -DnewVersion=1.1.0
mvn versions:commit
git add .
git commit -m "chore: bump version to 1.1.0"
git push
```

## Git Commit Message Conventions

Follow Conventional Commits for automatic changelog generation:

```
feat:  new feature
fix:   bug fix
docs:  documentation only
style: formatting changes
refactor: code refactoring
perf:  performance improvements
test:  test additions/changes
chore: build, deps, tooling
ci:    CI/CD workflow changes
```

**Examples**:
```
feat: add user authentication
fix: resolve NPE in user service
docs: update API documentation
chore: upgrade Spring Boot to 3.5.0
```

## Quick Commands Reference

```bash
# Create feature branch
git checkout -b feature/PROJ-123-description develop

# Create release branch
git checkout -b release/1.1.0 develop

# Create hotfix branch
git checkout -b hotfix/1.1.1 main

# View current version (uses Maven Git Versioning)
mvn help:evaluate -Dexpression=project.version -q -DforceStdout

# Check git status
git status

# View branch versioning
git log --oneline --all --graph
```

## Best Practices

1. **Always pull latest** before creating branches
2. **Use descriptive branch names** (feature/PROJ-123 not feature/xyz)
3. **Keep branches short-lived** (delete after merge)
4. **Write meaningful commit messages** (follows conventional commits)
5. **Don't force push** to main, develop, or release branches
6. **Test locally** before pushing to remote
7. **Use Pull Requests** for code review (all branches)
8. **Automate versioning** via git tags and branch patterns
9. **Tag releases** on main branch for version tracking
10. **Monitor GitHub Actions** for build/deploy status

## Troubleshooting

### "How do I see what version I'm on?"
```bash
mvn help:evaluate -Dexpression=project.version -q -DforceStdout
```

### "I accidentally committed to main"
Contact team lead to evaluate rollback strategy. Use GitHub's revert functionality.

### "Release branch won't deploy"
- Ensure branch name matches `release/X.Y.Z` pattern
- Check GitHub Actions logs for build failures
- Verify Java version and Maven cache state

### "Version not updating"
- Ensure `.mvn/maven-git-versioning-extension.xml` is configured
- Run `mvn clean` to clear cache
- Check `.git/config` for correct remote URL
