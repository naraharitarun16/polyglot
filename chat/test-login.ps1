$body = '{"username":"sa","password":"chat123"}'
Invoke-RestMethod -Method Post -Uri 'http://localhost:8081/auth/login' -ContentType 'application/json' -Body $body

