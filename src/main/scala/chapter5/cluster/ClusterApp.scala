package chapter5.cluster

import chapter5.cluster.Backend.Add

/**
  * Created by josgar on 20/11/2016.
  */
object ClusterApp extends App {

    //initiate frontend node
    Frontend.initiate()

    //initiate three nodes from backend
    Backend.initiate(2552)

    Backend.initiate(2560)

    Backend.initiate(2561)

    Thread.sleep(10000)

    Frontend.getFrontEnd ! Add(2, 4)

}
