/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.enemigos;

/**
 *
 * @author ellioth
 */
public interface Constantes {
    
    /* bandera para ir haciendo debug */
    public static final boolean DEBUG=false;
    
    /* cantidad de generaciones */
    public static final int AMOUNT_GENERATIONS=10000;
    public static final int MAXIMUN_GENERATION_PER_XML=500;
    /* tipo de mesclas y cambios con los hijos */
    public static final int NO_CHANGE=0;
    public static final int MUTATION=1;
    public static final int EXCHANGE=2;
    public static final int INVERT=3;
    public static final int PROBABILITY_CHANGE_SON=87;
    public static final int PROBABILITY_CHANGE_POPULATION_SIZE=65;
    public static final int DESPLAZAMIENTO=16;
    public static final int MIN_VARIANCE=5;
    
    /* numeros de multiproposito */
    public static final int CERO=0;
    public static final int UNO=1;
    public static final int DOS=2;
    public static final int TRES=3;
    public static final int CUATRO=4;
    public static final int CINCO=5;
    public static final int SEIS=6;
    public static final int SIETE=7;
    public static final int OCHO=8;
    public static final int DIEZ=10;
    
    /* tipos de enemigos que hay en el algoritmo genetico */
    public static final int OGRO=1;
    public static final int ELFO=2;
    public static final int HARPIA=3;
    public static final int MERCENARIO=4;
    
    /* limites para la esocgencia del tamaño de la vida*/
    public static final int LIFE_LOW_BOUND=1;
    public static final int LIFE_HIGH_BOUND=100;
    
    /* resistencias a los ataques de los aliados*/
    public static final int ARROW_HIGH=2;
    public static final int ARROW_LOW=1;
    public static final int MAGIC_HIGH=2;
    public static final int MAGIC_LOW=1;
    public static final int ARMOUR_HIGH=2;
    public static final int ARMOUR_LOW=1;
    
    /* velocidades */
    public static final int SPEED_HIGH=3;
    public static final int SPEED_MID=2;
    public static final int SPEED_LOW=1;
    
    /* limites en tamaños de oleadas de enemigos*/
    public static final int POPULATION_LOW_BOUND=15;
    public static final int POPULATION_HIGH_BOUND=40;
    
    /* etiquetas para el xml*/
    public static final String ENEMY="enemy";
    public static final String TYPE="type";
    public static final String SPEED="speed";
    public static final String LIFE="life";
    public static final String ARROW_RESISTANCE="arrowRes";
    public static final String MAGIC_RESISTANCE="magicRes";
    public static final String ARMOUR_RESISTANCE="armourRes";
    public static final String ID="id";
    public static final String GENERATIONS="Generations";
    public static final String GENERATION="Generation";
    
    /**
     * metodo para generar numeros al azar en un intervalo de numeros 
     * inclusivos.
     * @param pMinimo dato del tipo entero que es el numero minimo del 
     * intervalo.
     * @param pMaximo dato del tipo entero que es el numero maximo del 
     * intervalo.
     * @return retorna un numero del tipo entero que es el numero al azar
     * generado.
     */
    default int generaNumeroAleatorio(int pMinimo, int pMaximo){
        int num=(int)Math.floor(Math.random()*(pMinimo-(pMaximo+1))+(pMaximo+1));
        return num;
    }
    
    /**
     * metodo para realizar una mescla de bits entre dos numeros;
     * el primer numero se le hace un corrimiento de una cantidad de 
     * espacios especificados de derecha a izquierda y el segundo igual pero
     * se corre la misma cantidad de bits una vez mas pero de izquierda a
     * derecha y se aplica una 'or' (|) entre estos dos numeros y el 
     * resultado de esta operacion es lo que se devuelve.
     * @param pFirstNumber primero numero para la mezcla.
     * @param pSecondNumber segundo numero para la mezcla.
     * @return retorna el resultado de la mezcla de los bits.
     */
    default int mergeBits(int pFirstNumber, int pSecondNumber){
        int temp=0;
        pFirstNumber=pFirstNumber<<DESPLAZAMIENTO;
        pSecondNumber=pSecondNumber<<DESPLAZAMIENTO;
        pSecondNumber=pSecondNumber>>>DESPLAZAMIENTO;
        temp=pFirstNumber|pSecondNumber;
        //temp=pFirstNumber^pSecondNumber;
        return temp;
    }
    
    /**
     * recibe un numero y a este le aplica una compuerta inversora (~) con
     * la cual se puede invertir los bits del dato y el resultado de este es
     * lo que se retorna.
     * @param pNumber numero que queremos invertir.
     * @return el numero con sus bits invertidos.
     */
    default int invertingBits(int pNumber){
        pNumber=~pNumber;
        return pNumber;
    }
    
    /**
     * metodo para realizar una mutacion de bits en un numero;
     * el primer numero se le hace un corrimiento de una cantidad de 
     * espacios especificados de derecha a izquierda y el segundo numero 
     * es un random que esta en un intervalo de 1-100000 y es igual que 
     * el primero pero luego se corre la misma cantidad de bits una vez 
     * mas, de izquierda a derecha y se aplica una 'or' (|) entre estos
     * dos numeros y el resultado de esta operacion es lo que se devuelve.
     * @param pNumber numero que vamos a mutar.
     * @return 
     */
    default int mutatingBits(int pNumber){
        int newRandomNumber=generaNumeroAleatorio(UNO, 10000);
        //pNumber= pNumber<<DESPLAZAMIENTO;
        newRandomNumber=newRandomNumber<<DESPLAZAMIENTO;
        newRandomNumber=newRandomNumber>>>DESPLAZAMIENTO;
        int temp=pNumber^newRandomNumber;
        return temp;
    }
    
    /**
     * metodo para realizar un intercambio de bits en un numero;
     * el primer numero se le hace un corrimiento de una cantidad de 
     * espacios especificados de derecha a izquierda y el segundo es un 
     * numero al azar entre 1-100000 y es que el primero igual pero 
     * luego se corre la misma cantidad de bits una vez mas, de 
     * izquierda a derecha, luego se corre la mitad del desplazamiento 
     * de derecha a izquierda para asegurar que quede en un espacio en 
     * especifico y no los bordes se aplica una 'or' (|) entre estos dos 
     * numeros y el resultado de esta operacion es lo que se devuelve.
     * @param pNumber numero que vamos a aplicarle intercambio de bits.
     * @return retorna un numero con los bits intercambiado.
     */
    default int exchangingBits(int pNumber){
        int newRandomNumber=generaNumeroAleatorio(UNO, 10000);
        pNumber= pNumber<<DESPLAZAMIENTO;
        newRandomNumber=newRandomNumber<<DESPLAZAMIENTO;
        newRandomNumber=newRandomNumber>>>(DESPLAZAMIENTO/DOS);
        //newRandomNumber=newRandomNumber<<(DESPLAZAMIENTO/DOS);
        int temp=pNumber^newRandomNumber;
        return temp;
    }
    
    /**
     * si es mayor se le aplica el complemento a la base y se le da una suma 
     * de +1 para que quede dentro del rango, si es un cero se le hace un +1.
     * @param pNumber numero que vamos a revizar. 
     * @param pLimit limite con el que vamos a revizar.
     * @return retorna el numero dentro de los limites
     */
    default int checkNewNumGenerate(int pNumber, int pLimit){
        if(pNumber<CERO)
            pNumber=pNumber^-UNO;
        if(pNumber>pLimit){
            pNumber%=pLimit;
            pNumber++;
        }
        else if(pNumber==CERO)
            pNumber++;
        return pNumber;
    }
}
