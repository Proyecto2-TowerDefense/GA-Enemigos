/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.enemigos;

/**
 * clase para crear un algoritmo genetico que cree oleadas de enemigos 
 * y los guarde estos en un archivo Xml que se encuentra dentro de la carpeta
 * del proyecto.
 * @author ellioth
 */
public class GAEnemigos implements Constantes{
    
    private int _actualPopulationSize;
    private Enemigo[] _poblacion;
    
    /**
     * constructor de la clase del algoritmo genetico que creara las 
     * oleadas de los enemigos.
     */
    public GAEnemigos(){
        _actualPopulationSize=POPULATION_LOW_BOUND;
        _poblacion= new Enemigo[_actualPopulationSize];
        runGA();
    }
    
    /**
     * una vez invocado este metodo se genera la reproduccion hasta llegar
     * a un cierto limite de cantidad de generaciones necesarias segun 
     * se hayan definido.
     */
    private void runGA(){
        /* se va creando la primera generacion de individuos */
        int type, life;
        for(int i =0; i<_actualPopulationSize; i++){
            type= generaNumeroAleatorio(OGRO, MERCENARIO);
            life= generaNumeroAleatorio(LIFE_LOW_BOUND, LIFE_HIGH_BOUND/DOS);
            if(type==OGRO){
                if(DEBUG)
                    System.out.println("Creado un ogro en primera "
                            + "generacion");
                _poblacion[i]=new Enemigo(type, life, SPEED_LOW, ARROW_HIGH, 
                        MAGIC_LOW, ARMOUR_LOW);
            }else if(type==ELFO){
                if(DEBUG)
                    System.out.println("Creado un elfo en primera "
                            + "generacion");
                _poblacion[i]=new Enemigo(type, life, SPEED_HIGH, ARROW_LOW, 
                        MAGIC_HIGH, ARMOUR_LOW);
            }else if(type==HARPIA){
                if(DEBUG)
                    System.out.println("Creada una harpia en primera "
                            + "generacion");
                _poblacion[i]=new Enemigo(type, life, SPEED_MID, ARROW_HIGH, 
                        MAGIC_HIGH, CERO);
            }else if(type==MERCENARIO){
                if(DEBUG)
                    System.out.println("Creado un mercenario en primera "
                            + "generacion");
                int speed= generaNumeroAleatorio(SPEED_LOW, SPEED_HIGH);
                _poblacion[i]=new Enemigo(type, life, speed, ARROW_HIGH, 
                        MAGIC_LOW, ARMOUR_HIGH);
            }
        }
        //le ingresamos la poblacion y la agregamos al Xml
        CreateXml newXml= new CreateXml("myXml.xml");
        newXml.ingresarNuevaPobalcion(CERO, _poblacion, _actualPopulationSize);
        for(int i =1; i<AMOUNT_GENERATIONS; i++){
            //revisamos que la varianza de la poblacion no presente un 
            //estancamiento
            reproduccion(i);
            newXml.ingresarNuevaPobalcion(i, _poblacion, _actualPopulationSize);
            if(DEBUG)
                System.out.println("Revizando la variabilidad");
            //se revizara como esta la varianza entre todos los datos.
            if(getVariance()<MIN_VARIANCE && i<(AMOUNT_GENERATIONS/DOS)){
                //se alteraran N-1 individuos.
                if(DEBUG)
                    System.out.println("Varianza es de: "+getVariance());
                for(int index=1; index<_actualPopulationSize;index++){
                    _poblacion[index]=mutacion(_poblacion[index], 
                            _poblacion[index].getLife(), 
                            _poblacion[index].getSpeed(), 
                            _poblacion[index].getArrowRes(), 
                            _poblacion[index].getMagicRes(), 
                            _poblacion[index].getAmourRes(), 
                            _poblacion[index].getEnemyType());
                }
            }
            if(i%MAXIMUN_GENERATION_PER_XML==CERO){
                newXml.generateXml();
                String tempXmlName="myXml";
                int newIndexXml=i/MAXIMUN_GENERATION_PER_XML;
                tempXmlName=tempXmlName+String.valueOf(newIndexXml)+".xml";
                newXml= new CreateXml(tempXmlName);
            }
            
        }
        //creamos el nuevo xml
        newXml.generateXml();
        if(DEBUG){
            int i=0;
            for(; i<_actualPopulationSize; i++){
                if(getFitness(_poblacion[i])<=100)
                    break;
            }
            System.out.println("El mejor inidividuo es:"+
                    _poblacion[i].getEnemyType()+", con vida:"+
                    _poblacion[i].getLife()+ ", velocidad:"+
                    _poblacion[i].getSpeed()+ ", Res: "+
                    _poblacion[i].getAmourRes()+","+
                    _poblacion[i].getArrowRes()+","+
                    _poblacion[i].getMagicRes());
        }
        /*
        if(DEBUG){
            int i=0;
            for(; i<_actualPopulationSize; i++){
            System.out.println("El mejor inidividuo es:"+
                    _poblacion[i].getEnemyType()+", con vida:"+
                    _poblacion[i].getLife()+ ", velocidad:"+
                    _poblacion[i].getSpeed()+ "Res: "+
                    _poblacion[i].getAmourRes()+","+
                    _poblacion[i].getArrowRes()+","+
                    _poblacion[i].getMagicRes());
            }
        }*/
        
    }
    
    /**
     * una vez invocado este metodo se escogen los padres para la creacion 
     * del nuevo hijo.
     * @param pGeneration es el numero de la generacion que estamos haciendo.
     */
    private void reproduccion(int pGeneration){
        int [] padresFit=getPosPadre();
        Enemigo [] temp= new Enemigo[_actualPopulationSize*DOS];
        for(int i =0; i<_actualPopulationSize*DOS; i+=DOS){
            //rand para primer padre
            int randFit= generaNumeroAleatorio(CERO, padresFit[
                    _actualPopulationSize-UNO]);
            //obtener la posicion del primer padre
            int padrePos=getPadreToReproduce(randFit,padresFit,_actualPopulationSize);
            //establecer el primer padre
            temp[i]=_poblacion[padrePos];
            //rand para segundo padre
            randFit=generaNumeroAleatorio(CERO, padresFit[
                    _actualPopulationSize-UNO]);
            //obtener la posicion del segundo padre
            padrePos=getPadreToReproduce(randFit,padresFit,padrePos);
            //establecer el segundo padre
            temp[i+UNO]= _poblacion[padrePos];
        }
        Enemigo [] hijos= new Enemigo[_actualPopulationSize];
        int posPadre=0, posHijo=0;
        for(; posPadre<(_actualPopulationSize*DOS); posHijo++, posPadre+=DOS){
            int probabilidad=generaNumeroAleatorio(CERO,100);
            int typeOfChange=NO_CHANGE;
            if(probabilidad>PROBABILITY_CHANGE_SON)
                typeOfChange= generaNumeroAleatorio(MUTATION, INVERT);
            hijos[posHijo]=reproduccion(temp[posPadre],temp[posPadre+UNO],hijos[posHijo],
                    typeOfChange);
        }
        merge(hijos);   
    }
    
    /**
     * Cuando se invoca este metodo, se hace una reproduccion entre el 
     * material genetico de dos padres y forman a un hijo, y tambien se
     * le indica a este que tipo cambio va a sufrir el hijo segun el 
     * tipo de reproduccion que se le indique, ya sea una normal, mutante,
     * inversiva o de intercambio.
     * @param pPadre1 dato del primer padre para la reproduccion.
     * @param pPadre2 dato del segundo padre para la reproduccion.
     * @param pHijo dato del segundo hijo para la reproduccion.
     * @param pTypeOfReproduction dato entero del tipo de reproduccion a 
     * generar.
     */
    private Enemigo reproduccion(Enemigo pPadre1, Enemigo pPadre2, Enemigo pHijo,
            int pTypeOfReproduction){
        int speed, life, amourRes, magicRes, arrowRes;
        /**
         * se crean los nuevos atrubutos de los hijos y cada condicion
         * de estas se usa para verificar que los datos del nuevo hijo 
         * no se salga de los limites establecidos.
         */
        //nueva velocidad del hijo.
        speed=mergeBits(pPadre1.getSpeed(), pPadre2.getSpeed());
        speed=checkNewNumGenerate(speed, SPEED_HIGH);
        
        //mescla para hacer la nueva vida
        life=mergeBits(pPadre1.getLife(), pPadre2.getLife());
        life=checkNewNumGenerate(life, LIFE_HIGH_BOUND);
        
        //mescla de datos para la nueva resistencia a armeria
        amourRes=mergeBits(pPadre1.getAmourRes(), pPadre2.getAmourRes());
        amourRes=checkNewNumGenerate(amourRes, ARMOUR_HIGH);
        
        //mescla para hacer la nueva resistencia a magia
        magicRes=mergeBits(pPadre1.getMagicRes(), pPadre2.getMagicRes());
        magicRes=checkNewNumGenerate(magicRes, MAGIC_HIGH);
        
        //mescla para hacer la nueva resitencia a flechas
        arrowRes=mergeBits(pPadre1.getArrowRes(), pPadre2.getArrowRes());
        arrowRes=checkNewNumGenerate(arrowRes, ARROW_HIGH);
        
        if(pTypeOfReproduction==NO_CHANGE){
            if(DEBUG)
                System.out.println("Generando hijo simple");
            pHijo= new Enemigo(pPadre2.getEnemyType(), life, speed, arrowRes, 
                    magicRes, amourRes);
            return pHijo;
        }else if(pTypeOfReproduction==MUTATION){
            if(DEBUG)
                System.out.println("Generando mutacion");
            pHijo=mutacion(pHijo, life, speed, arrowRes, 
                    magicRes, amourRes, pPadre2.getEnemyType());
            return pHijo;
        }else if(pTypeOfReproduction==INVERT){
            if(DEBUG)
                System.out.println("Generando intercambio");
            pHijo= intervesion(pHijo, life, speed, arrowRes, 
                    magicRes, amourRes, pPadre2.getEnemyType());
            return pHijo;
        }else if(pTypeOfReproduction==EXCHANGE){
            if(DEBUG)
                System.out.println("Generando intercmabio");
            pHijo =intercambio(pHijo, life, speed, arrowRes, 
                    magicRes, amourRes, pPadre2.getEnemyType());
            return pHijo;
        }return null;
    }
    
    /**
     * Cuando este metodo es invocado provoca que se realice una mutacion
     * en el material genetico del hijo.
     * @param pHijo dato tipo Enemigo del nuevo hijo.
     * @param pLife entero de la nueva vida del hijo.
     * @param pSpeed entero de la nueva velocidad del hijo.
     * @param pArrowRes entero de la nueva resistecia a las flechas.
     * @param pMagicRes entero de la nueva resistencia a la magia.
     * @param pAmourRes entero de la nueva resistencia a la armeria.
     * @param pEnemyType entero del nuevo tipado del hijo.
     */
    private Enemigo mutacion(Enemigo pHijo, int pLife, int pSpeed, int pArrowRes,
            int pMagicRes, int pAmourRes, int pEnemyType){
        /**
         * se mutan los nuevos datos y se comprueba que estos no se
         * salgan de los limites prestablecidos.
         */
        pLife=mutatingBits(pLife);
        //mescla para hacer la nueva vida
        pLife=checkNewNumGenerate(pLife, LIFE_HIGH_BOUND);
        
        //mescla para hacer la nueva velocidad
        pSpeed=mutatingBits(pSpeed);
        pSpeed=checkNewNumGenerate(pSpeed, SPEED_HIGH);
        
        //mescla para hacer la nueva resitencia a flechas
        pArrowRes=mutatingBits(pArrowRes);
        pArrowRes=checkNewNumGenerate(pArrowRes, ARROW_HIGH);
        
        pMagicRes=mutatingBits(pMagicRes);
        //mescla para hacer la nueva resistencia a magia
        pMagicRes=checkNewNumGenerate(pMagicRes, MAGIC_HIGH);
        
        //mescla de datos para la nueva resistencia a armeria
        pAmourRes=mutatingBits(pAmourRes);
        pAmourRes=checkNewNumGenerate(pAmourRes, ARMOUR_HIGH);
        
        pHijo= new Enemigo(pEnemyType, pLife, pSpeed, pArrowRes, pMagicRes, 
                pAmourRes);
        return pHijo;
    }
    
    /**
     * cuando se invoca a este metodo en el algoritmo proboca que los 
     * atributos que se le van a conceder al hijo se intercambien generando
     * la variacion necesaria para el hijo
     * @param pHijo dato tipo Enemigo del nuevo hijo.
     * @param pLife entero de la nueva vida del hijo.
     * @param pSpeed entero de la nueva velocidad del hijo.
     * @param pArrowRes entero de la nueva resistecia a las flechas.
     * @param pMagicRes entero de la nueva resistencia a la magia.
     * @param pAmourRes entero de la nueva resistencia a la armeria.
     * @param pEnemyType entero del nuevo tipado del hijo.
     */
    private Enemigo intercambio(Enemigo pHijo, int pLife, int pSpeed, int pArrowRes,
            int pMagicRes, int pAmourRes, int pEnemyType){
        /**
         * se intercambian los nuevos datos y se comprueba que estos 
         * no se salgan de los limites prestablecidos.
         */
        pLife=exchangingBits(pLife);
        //mescla para hacer la nueva vida
        pLife=checkNewNumGenerate(pLife, LIFE_HIGH_BOUND);
        
        pSpeed=exchangingBits(pSpeed);
        pSpeed=checkNewNumGenerate(pSpeed, SPEED_HIGH);
        
        //mescla para hacer la nueva resitencia a flechas
        pArrowRes=exchangingBits(pArrowRes);
        pArrowRes=checkNewNumGenerate(pArrowRes, ARROW_HIGH);
        
        //mescla para hacer la nueva resistencia a magia
        pMagicRes=exchangingBits(pMagicRes);
        pMagicRes=checkNewNumGenerate(pMagicRes, MAGIC_HIGH);
        
        //mescla de datos para la nueva resistencia a armeria
        pAmourRes=exchangingBits(pAmourRes);
        pAmourRes=checkNewNumGenerate(pAmourRes, ARMOUR_HIGH);
        
        pHijo= new Enemigo(pEnemyType, pLife, pSpeed, pArrowRes, 
                pMagicRes, pAmourRes);
        return pHijo;
    }
    
    /**
     * metodo para realizar una inversion de losa atributos de los hijos, si
     * existe la posibilidad de que el atributo se salga de los limites se le 
     * aplica el complemento a la base y si es negativo se multiplica por un 
     * -1.
     * @param pHijo dato tipo Enemigo del nuevo hijo.
     * @param pLife entero de la nueva vida del hijo.
     * @param pSpeed entero de la nueva velocidad del hijo.
     * @param pArrowRes entero de la nueva resistecia a las flechas.
     * @param pMagicRes entero de la nueva resistencia a la magia.
     * @param pAmourRes entero de la nueva resistencia a la armeria.
     * @param pEnemyType entero del nuevo tipado del hijo.
     */
    private Enemigo intervesion(Enemigo pHijo, int pLife, int pSpeed, int pArrowRes,
            int pMagicRes, int pAmourRes, int pEnemyType){
        //se invierte
        pLife=invertingBits(pLife);
        //si es mayor se le aplica el complemento a la base y se le da una 
        //suma de +1 para que quede dentro del rango.
        pLife=checkNewNumGenerate(pLife, LIFE_HIGH_BOUND);
        
        pSpeed=invertingBits(pSpeed);
        pSpeed=checkNewNumGenerate(pSpeed, SPEED_HIGH);
        
        pArrowRes=invertingBits(pArrowRes);
        pArrowRes=checkNewNumGenerate(pArrowRes, ARROW_HIGH);
        
        pMagicRes=invertingBits(pMagicRes);
        pMagicRes=checkNewNumGenerate(pMagicRes, MAGIC_HIGH);
        
        pAmourRes=invertingBits(pAmourRes);
        pAmourRes=checkNewNumGenerate(pAmourRes, ARMOUR_HIGH);
        
        pHijo= new Enemigo(pEnemyType, pLife, pSpeed, pArrowRes, 
                pMagicRes, pAmourRes);
        return pHijo;
    }
    
    /**
     * devuelve un arreglo de entero de los padres para saber como seleccionar
     * los padres segun un ordenamiento de los fitness y mientras mas grande 
     * sea la diferencia del fitness o un padre con un fitness muy grande puede
     * que ese sea el mas probable a escogerce.
     * @return retorna el arreglo que contiene la escala de los padres de las 
     * posiciones segun su fitness.
     */
    private int[] getPosPadre(){
        int cant=0;
        int [] newArrayPadres=new int[_actualPopulationSize];
        for(int i=CERO; i<_actualPopulationSize;i++){
            newArrayPadres[i]=cant;
            cant+=(getFitness(_poblacion[i]));
        }
        return newArrayPadres;
    }
    
    /**
     * devuelve el indice del siguiente padre a escoguer y se asegura de que no
     * haya una clonacion con dos padres iguales.
     * @param pPadre1Fit fitness del padre que se escogio.
     * @param pPadresArray arreglo de entero del fitness de los padres para 
     * poder escoger el nuevo padre.
     * @param pPadreDeselect indice del padre que ya se esocogio para no 
     * realizar una clonacion.
     * @return retorna el nuevo padre para reproducir.
     */
    private int getPadreToReproduce(int pPadre1Fit, int[] pPadresArray, 
            int pPadreDeselect){
        int i =CERO;
        for(; i<_actualPopulationSize-UNO;i++){
            if(i==(_actualPopulationSize-DOS))
                break;
            if(pPadresArray[i]<=pPadre1Fit && pPadre1Fit<pPadresArray[i+UNO])
                break;
        }
         if(i==pPadreDeselect){
            if(i==(_actualPopulationSize-UNO))
                i--;
            else i++;
        }
        return i;
    }
    
    /**
     * algoritmo para mezclar los hijos con los padres y se 
     * encarga de ordenarlos.
     * @param pHijos arreglo de hijos con los cuales vamos a hacer la mezcla
     * con los padres para la nueva generacion.
     */
    private void merge(Enemigo[] pHijos){
        Enemigo[] temp=new Enemigo[_actualPopulationSize*DOS];
        if(DEBUG)
            for(int i =0; i<_actualPopulationSize; i++){
                System.out.println(getFitness(pHijos[i]));
            }
        //se agregan al nuevo arreglo de individuos.
        for(int i =0, j=0; j<_actualPopulationSize; i+=2, j++){
            temp[i]=pHijos[j];
            temp[i+UNO]=_poblacion[j];
        }
        //se ordenan los individuos.
        InsertSort(temp,_actualPopulationSize*DOS);
        int i=0;
        if(DEBUG)
            System.out.println("-------");
        //se revisa la probalidad de crecimento de la poblacion
        int probabilityOfGrowPopulation=generaNumeroAleatorio(CERO, 100);
        if(probabilityOfGrowPopulation>PROBABILITY_CHANGE_POPULATION_SIZE)
            changingPopulationSize(temp);
        else{
            //se agregan los mejores a la poblacion de uso.
            for(; i<_actualPopulationSize; i++){
                _poblacion[i]=temp[i];
                if(DEBUG)
                    System.out.println(getFitness(_poblacion[i]));
            }
        }
    }
    
    /**
     * cuando se invoca este metodo provoca que la poblacion altere
     * su tamaño haciendo que hayan mas o menos enemigos en cada oleada.
     * @param pTempPupolation arreglo de objetos tipo enemigo.
     */
    private void changingPopulationSize(Enemigo [] pTempPupolation){
        //alteramos el tamaño de la poblacion
        int newActualPopulationSize=generaNumeroAleatorio(
                POPULATION_LOW_BOUND+UNO, POPULATION_HIGH_BOUND);
        if(DEBUG)
            System.out.println("Redimensionando a nueva generacion, tamaño:"
                    +newActualPopulationSize);
        //revisamos si el nuevo tamaño es mas grande que la suma de los hijos
        //con los padres
        if(newActualPopulationSize>(_actualPopulationSize*DOS)){
            Enemigo[] temp = new Enemigo[newActualPopulationSize];
            for(int i=0; i<(_actualPopulationSize*DOS); i++){
                temp[i]=pTempPupolation[i];
            }
            //agregamos los nuevos individuos que 
            int type, life;
            for(int i =_actualPopulationSize*DOS; i<newActualPopulationSize;
                    i++){
                type= generaNumeroAleatorio(OGRO, MERCENARIO);
                life= generaNumeroAleatorio(LIFE_LOW_BOUND, LIFE_HIGH_BOUND/DOS);
                if(type==OGRO){
                    temp[i]=new Enemigo(type, life, SPEED_LOW, ARROW_HIGH, 
                            MAGIC_LOW, ARMOUR_LOW);
                }else if(type==ELFO){
                    temp[i]=new Enemigo(type, life, SPEED_HIGH, ARROW_LOW, 
                            MAGIC_HIGH, ARMOUR_LOW);
                }else if(type==HARPIA){
                    temp[i]=new Enemigo(type, life, SPEED_MID, ARROW_HIGH, 
                            MAGIC_HIGH, CERO);
                }else if(type==MERCENARIO){
                    int speed= generaNumeroAleatorio(SPEED_LOW, SPEED_HIGH);
                    temp[i]=new Enemigo(type, life, speed, ARROW_HIGH, 
                            MAGIC_LOW, ARMOUR_HIGH);
                }
            }
            //ordenamos el nuevo arreglo de enemigos
            InsertSort(temp,newActualPopulationSize);
            //cambiamos las referecias de los datos y listo
            _poblacion=temp;
            _actualPopulationSize=newActualPopulationSize;
            if(DEBUG){
                for(int i=0; i<_actualPopulationSize; i++){
                        System.out.println(getFitness(_poblacion[i]));
                }
            }
            return;
        }
        //se llega aqui cuando el tamaño al cual vamos a cambiar es mayor
        //o menor al anterior tamaño de poblacion y es menor que el 
        //doble de este.
        Enemigo[] temp = new Enemigo[newActualPopulationSize];
        for(int i=0; i<newActualPopulationSize; i++){
            temp[i]=pTempPupolation[i];
            if(DEBUG)
                System.out.println(getFitness(temp[i]));
        }
        //establecemos la nueva poblacion
        _poblacion=temp;
        _actualPopulationSize=newActualPopulationSize;
    }
    
    /**
     * metodo para realizar un ordenamiento de los individuos 
     * usando un algoritmo tipo insert segun su fitness, ordena en 
     * orden decendente.
     * @param pPoblacion poblacion a la cual vamos a organizar.
     */
    private void InsertSort(Enemigo[] pPoblacion, int pSize){
        for (int i=UNO;i<pSize; i++){
            Enemigo bolaTemp=pPoblacion[i];
            int j=i;
            for(; j>CERO && (getFitness(bolaTemp)>getFitness(pPoblacion[j-UNO]));j--)
                    pPoblacion[j]=pPoblacion[j-UNO];
            pPoblacion[j]=bolaTemp;
        }
    }
    
    /**
     * metodo para obtener el fitness del individuo,
     * el fitness se usa de forma de que el la raiz del la suma 
     * del cuadrado de los atributos es lo que conforma el fitness.
     * @param pIndividuo el individuo que vamos a sacarle el fitness.
     * @return retorna un entero.
     */
    private int getFitness(Enemigo pIndividuo){
        double SpeedTemp= Math.pow(pIndividuo.getSpeed(),DOS);
        double LifeTemp= Math.pow(pIndividuo.getLife(),DOS);
        double ArrowTemp= Math.pow(pIndividuo.getArrowRes(),DOS);
        double MagicTemp= Math.pow(pIndividuo.getMagicRes(),DOS);
        double ArmourTemp= Math.pow(pIndividuo.getAmourRes(),DOS);
        return (int)Math.sqrt(SpeedTemp+LifeTemp+ArrowTemp+MagicTemp+
                ArmourTemp);
    }
    
    /**
     * metodo para revizar la variabilidad de los individuos de las
     * generaciones, este metodo solo va a revizar cuando se esten abajo
     * de la N/2 cantidad de generaciones.
     * @return retorna un booleano que indica falso si la varianza es 
     * mayor a lo que se espera y un true si la varianza es menor de lo
     * esperado.
     */
    private int getVariance(){
        int promedio=0;
        //se saca el promedio.
        for(int i=0; i<_actualPopulationSize; i++){
            promedio+=getFitness(_poblacion[i]);
        }
        promedio/=_actualPopulationSize;
        int varianzaCuadrado=0;
        //se saca la variaza cuadrada.
        for(int i =0; i<_actualPopulationSize;i++){
            varianzaCuadrado+=Math.pow(
                    getFitness(_poblacion[i])-promedio, DOS);
        }
        //se saca la varianza total.
        int varianza=(int)Math.sqrt(varianzaCuadrado/_actualPopulationSize);
        return varianza;
    }
    
    public static void main(String[] args) {
        GAEnemigos nuevo = new GAEnemigos();
        //System.out.println(-65532^-1);
    }
}