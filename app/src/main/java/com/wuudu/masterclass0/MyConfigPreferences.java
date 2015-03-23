package com.wuudu.masterclass0;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

// CLASE que gestiona el archivo de preferencias del usuario (Usuario, Alarma y Pedido)
// El archivo de preferencias es una lista de pares Clave-Valor
// Se puede leer y sobreescribir el valor de Valor conociendo su Clave
// ( Como en los mensajes.putData("Clave", Valor) y luego valor=mensajes.get("Clave") )
public class MyConfigPreferences {
	
	private static final String SHARED_PREF_FILE = "MasterClassConfigPrefs";
	private static final String KEY_ID = "userId"; // Id usuario
    private SharedPreferences sharedPrefs;
    private Editor prefsEditor; // El editor permite leer y escribir el archivo

    public MyConfigPreferences(Context context) {
    	// Abre el archivo en modo privado: sólo accesible desde la aplicación, no desde fuera
    	// Si el archivo no existe, lo crea
        this.sharedPrefs = context.getSharedPreferences(SHARED_PREF_FILE, 0);
        this.prefsEditor = sharedPrefs.edit();
    }   

    public void setDefaults() { // Opciones por defecto
    	prefsEditor.putInt( KEY_ID, 0 );
        prefsEditor.commit();
   }

    // Funciones de tipo SET para cambiar un valor y GET para leer el valor guardado

    public void setmyId ( int id){			// Id usuario
        prefsEditor.putInt( KEY_ID, id );
        prefsEditor.commit();
    }
    
    public int getmyId(){
    	return sharedPrefs.getInt( KEY_ID, 0);
    }
    
}
