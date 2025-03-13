import { spawnSync } from "child_process";

export class ExternalCommandExecutor {
  static exec(cmd: string, env?: NodeJS.ProcessEnv) {
    const isWin = process.platform === "win32";
    const shell = isWin ? { cmd: "cmd", arg: "/C" } : { cmd: "sh", arg: "-c" };

    env = { ...process.env, ...env };

    if (isWin) {
      cmd = cmd.replace("./mvnw", "mvnw");
    }

    console.log(`exec: ${cmd}`);

    spawnSync(shell.cmd, [shell.arg, cmd], {
      cwd: "../",
      env: env,
      stdio: "inherit",
    });
  }
}
