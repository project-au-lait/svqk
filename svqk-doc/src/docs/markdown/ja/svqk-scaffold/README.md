## プロジェクトの概要

SVQK ソースコードの自動生成ツールです。

## 使い方

### VSCodeTask での実行方法

このツールには、対応する VSCode タスクが設定されています。以下の手順で実行可能です。

1. **タスク一覧を表示する。**  
   メニューバーから **ターミナル → タスクの実行** を選択する。  
   または、Windows: `Ctrl + Shift + P` / Mac: `Cmd + Shift + P` でコマンドパレットを開き、`Tasks: Run Task` と入力する。

2. **タスク一覧の中から`generate`を選択する。**

3. **component**（コマンドの`--component`に対応）を入力し`Enter`を押す。（入力例：backend）

4. **entity の一覧**（コマンドの引数に対応）を入力し`Enter`を押す。（入力例：Entity1 Entity2 Entity3）

※コマンドの`--templateType`に対応する値は、使用している SVQK の Archetype によって決定され、本プロジェクトの`.yo-rc.json`内に保持されます。

### コマンドでの実行方法

本プロジェクトのディレクトリで以下のコマンド（オプション/引数の値は例）を実行する。

```sh
pnpm start --component backend --templateType skeleton Entity1 Entity2 Entity3
```

引数およびオプションについては次の項目を参照してください。

### 引数およびオプションの説明

- 引数に指定する値によって、出力の生成元となる Entity の一覧が決定されます。

- `--component`の値によって、出力されるコンポーネント(all、backend、integration-test、api-client、frontend、e2e-test)が決定されます。
- `--templateType`の値によって、出力されるファイルのテンプレート種別（arch または skeleton）が決定されます。

  **arch**と**skeleton**の違いは以下の通りです。

  - **arch**
    - 共通機能を使用するファイルを生成する。
  - **skeleton**
    - 共通機能を使用しないファイルを生成する。

## カスタマイズ方法

このツールは[Yeoman](https://yeoman.io/)を使用しています。

各種テンプレートをカスタマイズしたい時は、`svqk/svqk-scaff/generators/app/templates/`以下のファイルを変更してください。

実行スクリプトをカスタマイズしたい時は、以下の手順に従ってください。

1. `svqk/svqk-scaff/generators/app/templates/`以下の`index.ts`および`type.d.ts`を変更する。
2. `pnpm prepare`を実行する。
