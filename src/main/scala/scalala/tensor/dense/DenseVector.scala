/*
 * Distributed as part of Scalala, a linear algebra library.
 *
 * Copyright (C) 2008- Daniel Ramage
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110 USA
 */
package scalala;
package tensor.dense;

import collection.domain.IndexDomain;
import collection.generic.DomainMapCanMapValuesFrom;

import collection.dense.{DenseMutableDomainSeqLike,DenseMutableDomainSeq};
import tensor.{VectorLike,Vector};

/**
 * A DenseVector is backed by an array of doubles.
 *
 * @author dramage
 */
trait DenseVectorLike[+This<:DenseVector]
extends DenseMutableDomainSeqLike[Double,This]
with VectorLike[This];

/**
 * A DenseVector is backed by an array of doubles.
 *
 * @author dramage
 */
class DenseVector(data : Array[Double])
extends DenseMutableDomainSeq[Double](data)
with Vector with DenseVectorLike[DenseVector] {
//  override def copy = new DenseVector(data.clone);
}

object DenseVector {
  /**
   * Static constructor that creates a dense vector of the given size
   * initialized by elements from the given values list (looping if
   * necessary).
   */
  def apply(size : Int)(values : Double*) =
    new DenseVector(Array.tabulate(size)(i => values(i % values.length)));

  /** Tabulate a vector with the value at each offset given by the function. */
  def tabulate(size : Int)(f : (Int => Double)) =
    new DenseVector(Array.tabulate(size)(f));

  implicit object DenseVectorCanMapValuesFrom
  extends DomainMapCanMapValuesFrom[DenseVector,Int,Double,Double,DenseVector] {
    override def apply(from_ : DenseVector, fn : (Double=>Double)) = {
      val from: DenseVector = from_ // workaround for compiler crasher "scala.tools.nsc.symtab.Types$TypeError: value data$mcD$sp is not a member of type parameter From"
      val data = new Array[Double](from.size);
      var i = 0;
      while (i < data.length) {
        data(i) = fn(from.data(i));
        i += 1;
      }
      new DenseVector(data);
    }

    override def apply(from_ : DenseVector, fn : ((Int,Double)=>Double)) = {
      val from: DenseVector = from_ // workaround for compiler crasher "scala.tools.nsc.symtab.Types$TypeError: value data$mcD$sp is not a member of type parameter From"
      val data = new Array[Double](from.size);
      var i = 0;
      while (i < data.length) {
        data(i) = fn(i, from.data(i));
        i += 1;
      }
      new DenseVector(data);
    }
  }
}
