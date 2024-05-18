
GITEA_URL=http://localhost:3000
ADMIN_USER_NAME=caretaker
ADMIN_USER_PASSWORD=caretaker

# admin: Read and Write
# repository: Read and Write
# user: Read and Write
ADMIN_TOKEN_NAME=admin_token
ADMIN_TOKEN=${ADMIN_TOKEN:?You must assign ADMIN_TOKEN}

# Create a user
REPO_USER_NAME=repouser
REPO_USER_PASSWORD=repopassword
REPO_USER_EMAIL=repouser@example.org
GITEA_URL_ADMIN_USERS=${GITEA_URL}/api/v1/admin/users

# Create a repository on behalf of a user
REPO_NAME=repo
GITEA_URL_ADMIN_USERS_REPOS=${GITEA_URL}/api/v1/admin/users/${REPO_USER_NAME}/repos
