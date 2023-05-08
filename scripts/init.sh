--replSet rs0 --dbpath /data/db --bind_ip_all

mongosh admin <<-EOF
    rs.initiate();
EOF

mongosh admin <<-EOF
    db.runCommand({
        createRole: "listDatabases",
        privileges: [
            { resource: { cluster : true }, actions: ["listDatabases"]}
        ],
        roles: []
    });

    db.runCommand({
        createRole: "readChangeStream",
        privileges: [
            { resource: { db: "", collection: ""}, actions: [ "find", "changeStream" ] }
        ],
        roles: []
    });

    db.createUser({
        user: 'debezium',
        pwd: 'dbz',
        roles: [
            { role: "readWrite", db: "event" },
            { role: "read", db: "local" },
            { role: "listDatabases", db: "admin" },
            { role: "readChangeStream", db: "admin" },
            { role: "read", db: "config" },
            { role: "read", db: "admin" }
        ]
    });
EOF

echo "Created users"
