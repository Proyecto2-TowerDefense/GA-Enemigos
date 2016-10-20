/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.enemigos;

/**
 * clase para crear a los enemigos de uso en el algoritmo 
 * genetico.
 * @author ellioth
 */
public class Enemigo {
    
    private int _enemyType;
    private int _life;
    private int _speed;
    private int _arrowRes;
    private int _magicRes;
    private int _amourRes;
    
    /**
     * constructor de la clase que nos creara el nuevo enemigo, se le 
     * ingresan todos los atributos que se piden los cuales son, tipo de 
     * enemigo, vida, velocidad, resistencia a flechas, magia y armeria.
     * @param pTypeEnemy entero que es el tipo de enemigo.
     * @param pLife entero del nivel de vida del enemigo.
     * @param pSpeed entero de la velocidad.
     * @param pArrowRes resistencia a las flechas.
     * @param pMagicRes resistencia a la magia.
     * @param pAmourRes resistencia a la armeria.
     */
    public Enemigo(int pTypeEnemy, int pLife, int pSpeed, int pArrowRes, 
            int pMagicRes, int pAmourRes){
       _enemyType=pTypeEnemy;
       _life=pLife;
       _speed=pSpeed;
       _arrowRes=pArrowRes;
       _magicRes=pMagicRes;
       _amourRes=pAmourRes;
    }
    
    /**
     * metodo para obtener el nivel de vida que tiene 
     * el enemigo. 
     * @return retorna un entero.
     */
    public int getLife(){
        return _life;
    }
    
    /**
     * metodo para obtener el tipo de enemigo que es.
     * @return retorna un entero.
     */
    public int getEnemyType(){
        return _enemyType;
    }
    
    /**
     * metodo para obtener la velocidad del enemigo.
     * @return retorna un entero.
     */
    public int getSpeed(){
        return _speed;
    }
    
    /**
     * metodo para obtener la resistencia a las 
     * flechas.
     * @return retorna un entero.
     */
    public int getArrowRes(){
        return _arrowRes;
    }
    
    /**
     * metodo para obtener la resistencia a la magia.
     * @return retorna un entero.
     */
    public int getMagicRes(){
        return _magicRes;
    }
    
    /**
     * metodo para obtener la resistencia a 
     * armeria.
     * @return retorna un entero.
     */
    public int getAmourRes(){
        return _amourRes;
    }
}
