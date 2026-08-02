# Gitflow Quick Reference

## 📱 One-Liner Commands

### Feature Development
```bash
git checkout -b feature/PROJ-123-description develop && git push origin
```
**Version**: `1.1.0-PROJ-123-description-SNAPSHOT`

### Release
```bash
git checkout -b release/1.1.0 develop && git push origin
```
**Version**: `1.1.0-RC1` → `1.1.0` (after tag)

### Hotfix
```bash
git checkout -b hotfix/1.1.1 main && git push origin
```
**Version**: `1.1.1` (exact)

---

## 🔄 Complete Workflow Steps

### Feature Branch
```bash
# 1. Create
git checkout develop && git pull
git checkout -b feature/PROJ-123-my-feature

# 2. Work & Commit
git add .
git commit -m "feat: add my feature"
git push origin feature/PROJ-123-my-feature

# 3. PR → GitHub → Review → Approve → Merge
# 4. Branch auto-deleted, develop updated automatically
```

### Release Branch
```bash
# 1. Create release branch
git checkout develop && git pull
git checkout -b release/1.1.0
git push origin release/1.1.0

# 2. QA Testing (artifacts published as RC)
# 3. Fix any issues: git commit -m "fix: ..." && git push
# 4. PR to main → Approve → Merge
# 5. GitHub Actions:
#    - Creates tag v1.1.0
#    - Publishes release
#    - Updates develop to 1.1.1-SNAPSHOT
```

### Hotfix Branch
```bash
# 1. Create from main
git checkout main && git pull
git checkout -b hotfix/1.1.1
git push origin hotfix/1.1.1

# 2. Fix & commit
git commit -m "fix: critical issue"
git push origin hotfix/1.1.1

# 3. PR to main → Approve → Merge
# 4. GitHub Actions auto-tags and updates develop
```

---

## 📊 Version Map

| Where | What | Example |
|-------|------|---------|
| Feature branch | Dev snapshot | `1.1.0-feature-name-SNAPSHOT` |
| Develop branch | Snapshot | `1.1.0-SNAPSHOT` |
| Release branch | RC candidate | `1.1.0-RC1` |
| Main tag | Release | `v1.1.0` |
| Hotfix branch | Release version | `1.1.1` |

---

## ⚡ Commit Message Convention

```
feat:     New feature (minor bump)
fix:      Bug fix (patch bump)
docs:     Documentation changes
style:    Code formatting (no logic change)
refactor: Code refactoring
perf:     Performance improvements
test:     Test additions/updates
chore:    Build, dependencies, CI/CD
```

**Examples**:
```bash
git commit -m "feat: add JWT authentication"
git commit -m "fix: NPE in user service"
git commit -m "docs: update API endpoints"
git commit -m "chore: upgrade Spring Boot to 3.5.0"
```

---

## 🚀 Deployment Timeline

```
Feature Branch (1.1.0-feature-SNAPSHOT)
    ↓ PR → Merge
Develop Branch (1.1.0-SNAPSHOT)
    ↓ Create release/1.1.0
Release Branch (1.1.0-RC1) ← QA Testing
    ↓ PR → Merge to main
Main Branch (v1.1.0) ← PRODUCTION
```

---

## ❌ What NOT to Do

❌ Commit directly to `main` or `develop`  
❌ Force push to any shared branch  
❌ Manual version edits in pom.xml  
❌ Skip tests before PR  
❌ Merge to `main` without tag  
❌ Release with untested code  
❌ Feature branches longer than 2 weeks  
❌ Ignore GitHub Actions failures  

---

## ✅ Status Checks

Your workflows automatically:
- ✅ Run unit tests
- ✅ Run integration tests
- ✅ Build JAR artifacts
- ✅ Publish to GitHub Packages
- ✅ Validate branch naming
- ✅ Check code quality
- ✅ Create release notes
- ✅ Tag releases
- ✅ Update develop version

---

## 📈 Version Bump Examples

```
v1.0.0 (current release on main)
   ↓
develop branch: 1.0.1-SNAPSHOT (auto)
   ↓
Create release/1.1.0 (manual - new features)
   ↓
Release 1.1.0 published
   ↓
develop branch: 1.1.1-SNAPSHOT (auto)
```

---

## 🆘 Common Issues

**Version not changing?**
```bash
mvn clean
mvn help:evaluate -Dexpression=project.version -q -DforceStdout
```

**Need to fix something after release?**
```bash
git checkout -b hotfix/1.1.1 main
git commit -m "fix: issue"
git push origin hotfix/1.1.1
# Create PR to main
```

**Wrong branch name?**
```bash
git branch -m feature/old-name feature/new-name
git push origin --delete old-name
git push origin feature/new-name
```

---

## 🎯 Recommended Workflow

1. **Every morning**: Pull latest `develop`
2. **Daily**: Commit with meaningful messages
3. **Every few days**: Push feature branch updates
4. **Weekly**: Submit PR when feature is complete
5. **Monthly**: Initiate release process
6. **As needed**: Create hotfixes from `main`

---

See **GITFLOW.md** and **GITFLOW_SETUP.md** for detailed documentation.
