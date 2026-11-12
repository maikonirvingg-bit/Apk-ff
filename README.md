# Menu Sem Root — versão 1.5

Aplicativo Android independente, sem login e sem root. Não modifica o Free Fire.

## Compilar no Android Studio
1. Extraia o ZIP e abra a pasta `MenuSemRoot` no Android Studio.
2. Use JDK 17 e instale o Android SDK Platform 35.
3. Sincronize o Gradle (Android Gradle Plugin 8.7.3).
4. Selecione **Build > Build APK(s)**.
5. APK de depuração esperado: `app/build/outputs/apk/debug/app-debug.apk`.

## Recursos
- Seleção entre Free Fire e Free Fire MAX.
- Verificação da instalação e abertura do jogo oficial.
- Atualização automática do status ao retornar ao aplicativo.
- Botão de diagnóstico que copia versão do Android e disponibilidade do jogo (sem credenciais).
- Alto contraste, texto ampliado e preferências salvas localmente.

## Limitações
Não inclui aimbot, ESP, mods nem contorno de autenticação. Não foi compilado ou testado neste ambiente por ausência do Android SDK e Gradle.

## Compilar pela nuvem (GitHub Actions)
1. Crie um repositório GitHub e envie **o conteúdo da pasta MenuSemRoot** para a raiz do repositório, incluindo `.github/workflows/build-apk.yml` (pasta oculta).
2. Abra **Actions > Compilar APK Android > Run workflow**.
3. Aguarde a execução e baixe o artefato `MenuSemRoot-debug-apk`.
4. Extraia o ZIP do artefato e instale `app-debug.apk` no Android, após verificar a origem do arquivo.

Esta configuração automatiza a compilação; **não foi executada nem validada** neste ambiente. A instalação e as funções precisam ser testadas no aparelho. O APK gerado é uma versão debug assinada para testes.
