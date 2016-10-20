/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.enemigos;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * clase para hacer los Xmls que contendran a cada uno de los individuos de
 * cada una de las generaciones.
 * @author ellioth
 */
public class CreateXml implements Constantes{
    
    private String _xmlSource;
    private DocumentBuilderFactory docFactory;
    private DocumentBuilder docBuilder;
    private Document doc;
    private Element mainRootElement;
    
    /**
     * constructor de la clase, recibe una ubicacion con el paradero del 
     * Xml que vamos a usar para guardar los datos.
     * @param pXmlSource cadena de caracteres que vamos a usar para indicar
     * la direccion del archivo.
     */
    public CreateXml(String pXmlSource){
        try {
            _xmlSource=pXmlSource;
            //creamos el documento de Xml.
            docFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
            //creamos el nodo principal al que se va a agregar todos
            mainRootElement = doc.createElement(GENERATIONS);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CreateXml.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * metodo para ingresar una nueva poblacion, en todo caso de que no exista
     * el Xml, el crea uno nuevo y agrega a la poblacion en este lugar.
     * @param pGeneration recibe un numero de la generacion que estamos 
     * agregando.
     * @param pPoblacion reibe los objetos del arreglo de la poblacion.
     * @param pPopulationSize recibe un entero que es el tamaño de la poblacion.
     */
    public void ingresarNuevaPobalcion(int pGeneration, Enemigo [] pPoblacion, int pPopulationSize){
        if(pGeneration==CERO){
            ingresarPrimeraGeneracion(pPoblacion, pPopulationSize);
            return;
        }
        //creamos el nodo principal al que se va a agregar todos
        //mainRootElement = doc.createElement(GENERATIONS);
        Element temp = doc.createElement(GENERATION);
        temp.setAttribute(ID, String.valueOf(pGeneration));
        //doc.appendChild(mainRootElement);
        for(int i=0; i<pPopulationSize; i++){
            temp.appendChild(getEnemy(doc, i, pPoblacion[i]));
        }
        mainRootElement.appendChild(temp);
    }
    
    /**
     * metodo usado como respaldo para cuando no se tiene un Xml ya creado,
     * crea el nuevo Xml y le ingresa la primera generacion.
     * @param pPoblacion arreglo de los objetos de los enemigos.
     * @param pPopulationSize cantidad total de la poblacion con la cual 
     * estamos operando.
     */
    private void ingresarPrimeraGeneracion(Enemigo [] pPoblacion, int pPopulationSize){
        /*creamos el documento de Xml.
        docFactory = DocumentBuilderFactory.newInstance();
        docBuilder = docFactory.newDocumentBuilder();
        doc = docBuilder.newDocument();

        //creamos el nodo principal al que se va a agregar todos
        mainRootElement = doc.createElement(GENERATIONS);*/
        Element temp = doc.createElement(GENERATION);
        temp.setAttribute(ID, String.valueOf(CERO));
        //doc.appendChild(mainRootElement);
        for(int i=0; i<pPopulationSize; i++){
            temp.appendChild(getEnemy(doc, i, pPoblacion[i]));
        }
        mainRootElement.appendChild(temp);
    }
    
    /**
     * metodo para crear el individuo del enemigo el cual mostrara todos sus 
     * atributos y este tendra un ID para un facil recorrido entre los datos.
     * @param pDocument objeto del documento sobre el cual estamos escribiendo.
     * @param pID entero del id asignado al enemigo.
     * @param pEnemy objeto que contiene la informaicon del enemigo.
     * @return retorna el objeto del enemigo ya creado para agregarse al Xml.
     */
    private Element getEnemy(Document pDocument,int pID, Enemigo pEnemy ){
        //creamos el objeto que contiene los atributos del enemigo
        Element enemigo = pDocument.createElement(ENEMY);
        enemigo.setAttribute(ID, String.valueOf(pID));
        
        Element velocidad, vida, tipo, arrowRes, magicRes, armourRes;
        //creamos el objeto de velocidad
        velocidad= pDocument.createElement(SPEED);
        velocidad.appendChild(pDocument.createTextNode(String.valueOf(pEnemy.getSpeed())));
        enemigo.appendChild(velocidad);
        //creamos el objeto de vida
        vida= pDocument.createElement(LIFE);
        vida.appendChild(pDocument.createTextNode(String.valueOf(pEnemy.getLife())));
        enemigo.appendChild(vida);
        //creamos el objeto del tipado de enemigo
        tipo= pDocument.createElement(TYPE);
        tipo.appendChild(pDocument.createTextNode(String.valueOf(pEnemy.getEnemyType())));
        enemigo.appendChild(tipo);
        //creamos el objeto de la resistencia a flechas.
        arrowRes= pDocument.createElement(ARROW_RESISTANCE);
        arrowRes.appendChild(pDocument.createTextNode(String.valueOf(pEnemy.getArrowRes())));
        enemigo.appendChild(arrowRes);
        //creamos el objeto de la resistencia a la magia.
        magicRes= pDocument.createElement(MAGIC_RESISTANCE);
        magicRes.appendChild(pDocument.createTextNode(String.valueOf(pEnemy.getMagicRes())));
        enemigo.appendChild(magicRes);
        //creamos el objeto de la resistencia a la armeria.
        armourRes= pDocument.createElement(ARMOUR_RESISTANCE);
        armourRes.appendChild(pDocument.createTextNode(String.valueOf(pEnemy.getAmourRes())));
        enemigo.appendChild(armourRes);
        
        //retornamos el objeto del enemigo ya formado.
        return enemigo;
    }
    
    /**
     * Metodo para generar el nuevo Xml con los nuevos datos.
     */
    public void generateXml(){
        try {
            doc.appendChild(mainRootElement);
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(_xmlSource));
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            Logger.getLogger(CreateXml.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
