(function () {
  const dbName =
    (process && process.env && process.env.MONGO_APP_DB) || "skill-exchange";
  const user =
    (process && process.env && process.env.MONGO_APP_USER) || "skilleduser";
  const pass =
    (process && process.env && process.env.MONGO_APP_PASS) || "change_me";

  const appdb = db.getSiblingDB(dbName);
  const existing = appdb.getUser(user);

  if (existing) {
    print(`[init] Usuario '${user}' ya existe en DB '${dbName}'.`);
    return;
  }

  appdb.createUser({
    user: user,
    pwd: pass,
    roles: [{ role: "readWrite", db: dbName }],
  });

  print(`[init] Usuario '${user}' creado en DB '${dbName}'.`);
})();
