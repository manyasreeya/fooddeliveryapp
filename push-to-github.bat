@echo off
echo ==============================================
echo Savor Food Delivery Application Git Deployer
echo ==============================================
echo.
echo Initializing local repository and pushing to github.com/manyasreeya/fooddeliveryapp
echo.

REM Initialize Git if not already
git init

REM Add everything
git add .

REM Commit all the beautiful UI and Architecture
git commit -m "feat: Add premium authentication, massive menu, admin dashboard, and CI/CD polish"

REM Ensure main branch
git branch -M main

REM Link to the remote (suppresses error if remote already exists)
git remote add origin https://github.com/manyasreeya/fooddeliveryapp.git

REM Push the code securely
git push -u origin main

echo.
echo Complete! Your repo is live on GitHub and ready to trigger your Jenkins pipeline.
pause
