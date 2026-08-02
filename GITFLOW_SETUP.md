# Gitflow + Dynamic Versioning Setup Guide

This guide explains the complete Gitflow workflow with automatic versioning for your Spring Boot project.

## ✅ What's Been Set Up

### 1. **Maven Git Versioning Extension** (.mvn/maven-git-versioning-extension.xml)
   - Automatically determines version based on git branch/tags
   - Zero manual version management needed
   - Configured for all Gitflow branches

### 2. **GitHub Workflows** (.github/workflows/)
   - `feature-build.yml` - Feature branch CI/CD
   - `develop-snapshot.yml` - Develop branch snapshot publishing
   - `release.yml` - Release branch & RC publishing
   - `hotfix-build.yml` - Hotfix branch CI/CD
   - `main-release.yml` - Main branch release deployment
   - `pr-checks.yml` - Pull Request validation

### 3. **Documentation**
   - `GITFLOW.md` - Complete branching strategy guide

---

## 🚀 Quick Start Examples

### Example 1: Develop a New Feature

```bash
# Step 1: Create feature branch
git checkout -b feature/user-authentication develop

# Step 2: Make changes
git add .
git commit -m "feat: implement JWT authentication"
git push origin feature/user-authentication

# Version during development: 1.1.0-user-authentication-SNAPSHOT

# Step 3: Create Pull Request
# - GitHub Actions runs tests automatically
# - After approval, merge to develop
# - Branch automatically deleted

# Step 4: Develop branch now has new code
# - Snapshot builds published: 1.1.0-SNAPSHOT
```

### Example 2: Release to Production

```bash
# Step 1: Create release branch (when develop is stable)
git checkout -b release/1.1.0 develop
git push origin release/1.1.0

# Version: 1.1.0-RC1 (Release Candidate)
# - Artifacts published to GitHub Packages
# - Ready for QA testing

# Step 2: If issues found, fix them
git commit -m "fix: critical issue in release"
git push origin release/1.1.0

# Step 3: Create PR to main
# - After approval, merge creates release
# - Automatic tag: v1.1.0
# - Deploy to production

# Step 4: Branch auto-updates develop
# - develop gets v1.1.1-SNAPSHOT
# - Ready for next feature development
```

### Example 3: Emergency Production Hotfix

```bash
# Step 1: Create hotfix branch from main
git checkout -b hotfix/1.1.1 main
git push origin hotfix/1.1.1

# Version: 1.1.1
# - Critical fix only

# Step 2: Fix and commit
git commit -m "fix: critical production issue"
git push origin hotfix/1.1.1

# Step 3: Create PR to main
# - After approval, merges and tags
# - Version: 1.1.1 deployed to production

# Step 4: Develop updated
# - develop gets 1.1.2-SNAPSHOT
```

---

## 📋 Workflow Reference

| Branch | Trigger | Version | Publishing | Next Step |
|--------|---------|---------|------------|-----------|
| `feature/*` | Push | `1.1.0-feat-SNAPSHOT` | No | → PR to develop |
| `develop` | Push | `1.1.0-SNAPSHOT` | Yes (snapshot) | Testing |
| `release/*` | Push | `1.1.0-RC1` | Yes (RC) | → PR to main |
| `main` | Push | `1.1.0` | Yes (release) | Production |
| `hotfix/*` | Push | `1.1.1` | Yes (RC) | → PR to main |

---

## 🔧 Configuration Files

### .mvn/maven-git-versioning-extension.xml
Defines how versions are calculated:
- `main` → Uses git tags (e.g., v1.0.0)
- `develop` → Next version + SNAPSHOT
- `feature/*` → Next version + feature name + SNAPSHOT
- `release/*` → Exact version + RC1
- `hotfix/*` → Exact version

### .github/workflows/*.yml
Five workflows handle different scenarios:
1. **feature-build.yml** - Validates features before merge
2. **develop-snapshot.yml** - Publishes snapshots from develop
3. **release.yml** - Publishes Release Candidates
4. **main-release.yml** - Deploys releases from main
5. **hotfix-build.yml** - Handles emergency fixes
6. **pr-checks.yml** - Validates all PRs

---

## 💡 Key Concepts

### Version Determination (No Manual Versioning!)

The Maven Git Versioning Extension **automatically** sets versions based on:

1. **Which branch** you're on
2. **Git tags** on that branch
3. **Last released version** (from main tags)

**You never manually edit version numbers.**

### Automatic Version Increments

When you release version `1.0.0`:
- Main branch → `1.0.0` (from tag v1.0.0)
- Develop branch → `1.0.1-SNAPSHOT` (next patch)
- Feature branches → `1.0.1-featurename-SNAPSHOT`

### Semantic Versioning

Versions follow: `MAJOR.MINOR.PATCH`

- **MAJOR** (1.0.0 → 2.0.0): Breaking changes
- **MINOR** (1.0.0 → 1.1.0): New features
- **PATCH** (1.0.0 → 1.0.1): Bug fixes

---

## 🏗️ Branch Protection Rules (Recommended)

Set these in GitHub Settings → Branches → Branch Protection Rules:

### For `main` branch:
- ✅ Require pull request reviews before merging (2 reviewers)
- ✅ Require status checks to pass
- ✅ Require branches to be up to date
- ✅ Require code quality checks to pass
- ❌ Allow force pushes (NEVER)

### For `develop` branch:
- ✅ Require pull request reviews (1 reviewer)
- ✅ Require status checks to pass
- ✅ Require branches to be up to date
- ❌ Allow force pushes (NEVER)

### For `release/*` and `hotfix/*`:
- ✅ Require pull request reviews (1 reviewer)
- ✅ Require status checks to pass

---

## 📊 Common Workflows

### Day-to-Day Feature Development

```
1. Pull latest develop
   git checkout develop && git pull

2. Create feature branch
   git checkout -b feature/PROJ-123-awesome-feature

3. Develop and commit with conventional commits
   git commit -m "feat: add awesome feature"
   git commit -m "test: add tests for feature"

4. Push to GitHub
   git push origin feature/PROJ-123-awesome-feature

5. Create Pull Request
   - Go to GitHub, PR from feature branch to develop
   - Describe changes
   - Request reviewers

6. Address review feedback
   git commit -m "fix: address review comments"
   git push origin feature/PROJ-123-awesome-feature

7. Merge PR
   - Approved? Click "Merge Pull Request"
   - GitHub Actions tests run after merge
   - Branch automatically deleted

8. Pull updated develop
   git checkout develop && git pull
```

### Monthly Release Process

```
1. Determine release version
   Last version: 1.0.0 → New version: 1.1.0

2. Create release branch
   git checkout -b release/1.1.0 develop

3. Final QA testing
   - GitHub Actions publishes RC version
   - Artifacts in GitHub Packages
   - Test thoroughly

4. Fix any QA issues
   git commit -m "fix: QA issue in release"
   git push origin release/1.1.0

5. Create PR to main
   - Title: "Release version 1.1.0"
   - Describe major changes

6. After approval, merge to main
   - GitHub Actions creates tag v1.1.0
   - Publishes to production
   - Auto-updates develop with 1.1.1-SNAPSHOT

7. Verify production deployment
   - Check GitHub Packages
   - Verify application running
   - Monitor logs
```

### Emergency Hotfix Process

```
1. Create hotfix branch from main
   git checkout -b hotfix/1.1.1 main

2. Fix critical issue ONLY
   git commit -m "fix: critical production issue"
   git push origin hotfix/1.1.1

3. Create PR to main
   - Include detailed description
   - Assign to on-call engineer

4. After approval, merge to main
   - GitHub Actions creates tag v1.1.1
   - Published to production

5. Verify fix in production
   - Test the fix
   - Monitor for regressions

6. Auto-updates develop
   - develop becomes 1.1.2-SNAPSHOT
```

---

## 🆘 Troubleshooting

### Check Current Version
```bash
mvn help:evaluate -Dexpression=project.version -q -DforceStdout
```

### View Git Tags
```bash
git tag -l  # List all tags
git show v1.0.0  # See tag details
```

### View Git Log
```bash
git log --oneline --all --graph  # Visual branch history
git log --oneline develop..main  # Commits in main not in develop
```

### Merge Develop to Release
```bash
git checkout release/1.1.0
git merge develop
git push origin release/1.1.0
```

### Delete Old Feature Branch
```bash
git branch -d feature/old-feature  # Local delete
git push origin --delete feature/old-feature  # Remote delete
```

---

## 📚 Additional Resources

- [Git Versioning Maven Extension Docs](https://qoomon.github.io/maven-git-versioning-extension/)
- [Gitflow Cheatsheet](https://danielkummer.github.io/git-flow-cheatsheet/)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Semantic Versioning](https://semver.org/)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)

---

## ✨ Benefits of This Setup

✅ **No manual version management** - Git is source of truth
✅ **Consistent versioning** - Semantic versioning enforced
✅ **Automated deployments** - Reduces human error
✅ **Clear branch purpose** - Everyone knows what each branch is for
✅ **Pull request culture** - Code review built-in
✅ **Audit trail** - Git commits document everything
✅ **Parallel development** - Multiple features simultaneously
✅ **Safe releases** - RC testing before production
✅ **Emergency hotfixes** - Can fix production without interrupting develop
✅ **Snapshot artifacts** - Latest development always available

---

## 🎯 Next Steps

1. **Read GITFLOW.md** for detailed branching strategy
2. **Test with a feature branch** to familiarize yourself
3. **Configure branch protection rules** in GitHub (see above)
4. **Train team** on the new workflow
5. **Monitor GitHub Actions** for build failures
6. **Document any team-specific conventions** (e.g., PR naming)

---

## 📞 Questions?

Refer to:
- GITFLOW.md - Detailed strategy and examples
- GitHub Actions logs - Workflow execution details
- Maven Git Versioning docs - Version calculation rules
