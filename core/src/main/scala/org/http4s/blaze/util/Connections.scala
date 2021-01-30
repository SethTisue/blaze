package org.http4s.blaze.util

import java.util.concurrent.Semaphore

private[blaze] sealed trait Connections {
  def acquire(): Unit
  def release(): Unit
  def close(): Unit = release()
}

private[blaze] object Connections {

  def apply(maxConnections: Int): Connections =
    if (maxConnections < 0) Unbounded else new Bounded(maxConnections)

  private[this] object Unbounded extends Connections {
    override def acquire(): Unit = ()
    override def release(): Unit = ()
  }

  private[this] final class Bounded(maxConnections: Int) extends Connections {
    val semaphore = new Semaphore(maxConnections)
    override def acquire(): Unit = semaphore.acquire()
    override def release(): Unit = semaphore.release()
  }

}
