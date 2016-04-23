package pl.zazakretem.magisterka;

import java.util.ArrayDeque;

abstract class AbstractValue
{


    protected class Value {

        private final int LENGTH = 10;

        private ArrayDeque<Float> queue = new ArrayDeque<Float>();

        public void add(float value){
            if(value == 0) {
                return;
            }
            queue.add(value);
            if(queue.size() > LENGTH){
                queue.poll();
            }
        }

        public float getAveraged()
        {
            int size = queue.size();
            float sum = 0;
            for (float element : queue) {
                sum += element;
            }
            return sum / size;
        }

        public float getNext()
        {
            return queue.poll();
        }
    }
}
