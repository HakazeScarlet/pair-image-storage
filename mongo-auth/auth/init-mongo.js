db.createUser({
    user: 'admin',
    pwd: 'pass',
    roles: [
        {
            role: 'readWrite',
            db: 'image_db'
        }
    ]
});