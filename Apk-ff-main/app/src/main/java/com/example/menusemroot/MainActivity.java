package com.example.menusemroot;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.*;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;

public class MainActivity extends Activity {
    private LinearLayout root;
    private SharedPreferences prefs;
    private TextView status;
    private TextView title;
    private TextView note;
    private TextView gameLabel;
    private Switch contrastSwitch;
    private Switch textSwitch;
    private Spinner gameSelector;
    private TextView installedStatus;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences("menu", MODE_PRIVATE);
        ScrollView scroll = new ScrollView(this);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = (int)(24 * getResources().getDisplayMetrics().density);
        root.setPadding(pad, pad, pad, pad);
        scroll.addView(root);
        setContentView(scroll);

        title = new TextView(this);
        title.setText("MENU SEM ROOT");
        root.addView(title);
        note = new TextView(this);
        note.setText("Aplicativo independente • Sem login • Sem root\nNão modifica o Free Fire.");
        note.setPadding(0, pad / 2, 0, pad);
        root.addView(note);

        contrastSwitch = new Switch(this);
        contrastSwitch.setText("Tema de alto contraste");
        contrastSwitch.setChecked(prefs.getBoolean("contrast", false));
        root.addView(contrastSwitch);

        textSwitch = new Switch(this);
        textSwitch.setText("Texto ampliado");
        textSwitch.setChecked(prefs.getBoolean("large_text", false));
        root.addView(textSwitch);

        gameLabel = new TextView(this);
        gameLabel.setText("Escolha o jogo instalado:");
        root.addView(gameLabel);
        gameSelector = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item,
            new String[]{"Free Fire", "Free Fire MAX"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameSelector.setAdapter(adapter);
        gameSelector.setSelection(prefs.getInt("game", 0));
        gameSelector.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putInt("game", position).apply();
                updateInstalledStatus();
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
        root.addView(gameSelector);

        installedStatus = new TextView(this);
        root.addView(installedStatus);

        Button check = new Button(this);
        check.setText("Verificar jogo instalado");
        check.setOnClickListener(v -> updateInstalledStatus());
        root.addView(check);

        Button launch = new Button(this);
        launch.setText("Abrir Free Fire instalado");
        launch.setOnClickListener(v -> openGame());
        root.addView(launch);

        Button diagnostics = new Button(this);
        diagnostics.setText("Copiar diagnóstico do aparelho");
        diagnostics.setOnClickListener(v -> copyDiagnostics());
        root.addView(diagnostics);

        status = new TextView(this);
        status.setText("Configurações salvas no aparelho. Nenhuma conta necessária.");
        status.setPadding(0, pad, 0, pad / 2);
        root.addView(status);

        Button reset = new Button(this);
        reset.setText("Restaurar configurações");
        reset.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            contrastSwitch.setChecked(false);
            textSwitch.setChecked(false);
            gameSelector.setSelection(0);
            applyAppearance();
        });
        root.addView(reset);

        contrastSwitch.setOnCheckedChangeListener((button, checked) -> {
            prefs.edit().putBoolean("contrast", checked).apply();
            applyAppearance();
        });
        textSwitch.setOnCheckedChangeListener((button, checked) -> {
            prefs.edit().putBoolean("large_text", checked).apply();
            applyAppearance();
        });
        applyAppearance();
        updateInstalledStatus();
    }

    @Override protected void onResume() {
        super.onResume();
        updateInstalledStatus();
    }

    private void copyDiagnostics() {
        boolean max = gameSelector.getSelectedItemPosition() == 1;
        String pkg = max ? "com.dts.freefiremax" : "com.dts.freefireth";
        boolean installed = getPackageManager().getLaunchIntentForPackage(pkg) != null;
        String report = "Menu Sem Root 1.5\nAndroid: " + Build.VERSION.RELEASE
            + " (API " + Build.VERSION.SDK_INT + ")\nJogo: " + pkg
            + "\nDisponível para iniciar: " + (installed ? "sim" : "não");
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText("Diagnóstico Menu Sem Root", report));
            status.setText("Diagnóstico copiado. Cole na conversa para investigar problemas.");
        }
    }

    private void updateInstalledStatus() {
        if (installedStatus == null || gameSelector == null) return;
        boolean max = gameSelector.getSelectedItemPosition() == 1;
        String pkg = max ? "com.dts.freefiremax" : "com.dts.freefireth";
        String name = max ? "Free Fire MAX" : "Free Fire";
        boolean found = getPackageManager().getLaunchIntentForPackage(pkg) != null;
        installedStatus.setText(name + (found ? ": instalado e disponível para abrir." : ": não encontrado neste aparelho."));
    }

    private void openGame() {
        int selection = gameSelector.getSelectedItemPosition();
        String pkg = selection == 1 ? "com.dts.freefiremax" : "com.dts.freefireth";
        String name = selection == 1 ? "Free Fire MAX" : "Free Fire";
        Intent intent = getPackageManager().getLaunchIntentForPackage(pkg);
        if (intent == null) {
            status.setText(name + " não encontrado. Instale a versão oficial para abrir pelo menu.");
            return;
        }
        try {
            startActivity(intent);
            status.setText("Abrindo " + name + ".");
        } catch (RuntimeException e) {
            status.setText("Não foi possível abrir " + name + ". Tente iniciá-lo diretamente.");
        }
    }

    private void applyAppearance() {
        boolean contrast = contrastSwitch.isChecked();
        boolean large = textSwitch.isChecked();
        root.setBackgroundColor(contrast ? Color.BLACK : 0xFF11151F);
        title.setTextColor(Color.WHITE);
        note.setTextColor(contrast ? Color.WHITE : 0xFFBFC8D8);
        status.setTextColor(Color.WHITE);
        gameLabel.setTextColor(Color.WHITE);
        installedStatus.setTextColor(Color.WHITE);
        contrastSwitch.setTextColor(Color.WHITE);
        textSwitch.setTextColor(Color.WHITE);
        title.setTextSize(large ? 32 : 26);
        note.setTextSize(large ? 20 : 14);
        status.setTextSize(large ? 20 : 14);
        gameLabel.setTextSize(large ? 20 : 16);
        installedStatus.setTextSize(large ? 20 : 14);
        contrastSwitch.setTextSize(large ? 20 : 16);
        textSwitch.setTextSize(large ? 20 : 16);
    }
}
