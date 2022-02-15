package com.example.charlynegocios.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CriaArquivo {

    private File pasta;
    private File arquivo;
    private Context context;


    public CriaArquivo(File pasta, Context context){
        this.context = context;
        this.pasta = pasta;

        if(!pasta.exists()){
            pasta.mkdirs();
        }

    }


    public String SalvarPNG(Bitmap bitmap, String nomeDoArquivo){
        arquivo = new File(pasta, nomeDoArquivo+".png");
        try{
        arquivo.createNewFile();
            OutputStream streamDeSaida = new FileOutputStream(arquivo);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, streamDeSaida);
            streamDeSaida.close();
        }
        catch (IOException e){
            e.printStackTrace();
            return "Erro: " + e;
        }

        return "Sucesso";
    }

    public String SalvarPDF(Bitmap bitmap, String nomeDoArquivo){
        arquivo = new File(pasta, nomeDoArquivo+".pdf");

        PdfDocument arquivoPDF = new PdfDocument();
        PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(bitmap.getWidth(),bitmap.getHeight(),1).create();
        PdfDocument.Page pagina = arquivoPDF.startPage(info);
        Canvas canvas = pagina.getCanvas();


        canvas.drawBitmap(bitmap, null, new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),null);
        arquivoPDF.finishPage(pagina);

        try{
            arquivo.createNewFile();
            OutputStream streamDeSaida = new FileOutputStream(arquivo);
            arquivoPDF.writeTo(streamDeSaida);
            streamDeSaida.close();
            arquivoPDF.close();
        }
        catch (IOException e){
            e.printStackTrace();
            return "Erro: " + e;
        }

        return "Sucesso";
    }


}
