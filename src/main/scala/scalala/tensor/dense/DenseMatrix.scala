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

import collection.domain.TableDomain;
import collection.generic.DomainMapCanMapValuesFrom;

import collection.dense.{DenseMutableDomainTableLike,DenseMutableDomainTable};
import tensor.{MatrixLike,Matrix};

/**
 * A DenseMatrix is backed by an array of doubles, with each column
 * stored before the next column begins.
 *
 * @author dramage
 */
trait DenseMatrixLike[+This<:DenseMatrix]
extends DenseMutableDomainTableLike[Double,This]
with MatrixLike[This];

/**
 * A DenseMatrix is backed by an array of doubles, with each column
 * stored before the next column begins.
 *
 * @author dramage
 */
class DenseMatrix(numRows : Int, numCols : Int, data : Array[Double])
extends DenseMutableDomainTable[Double](numRows, numCols, data)
with Matrix with DenseMatrixLike[DenseMatrix] {
//  override def copy = new DenseMatrix(numRows, numCols, data.clone);
}

object DenseMatrix {
  /**
   * Static constructor that creates a dense matrix of the given size
   * initialized by iterator from the given values list (looping if
   * necessary).  The values are initialized column-major, i.e. values
   * is read in order to populate the matrix, filling up column 0 before
   * column 1, before column 2 ...
   */
  def apply(rows : Int, cols : Int)(values : Double*) = {
    new DenseMatrix(rows, cols, Array.tabulate(rows * cols)(i => values(i % values.length)));
  }

  /** Tabulate a matrix from a function from row,col position to value. */
  def tabulate(rows : Int, cols : Int)(fn : (Int, Int) => Double) = {
    new DenseMatrix(rows, cols, Array.tabulate(rows * cols)(i => fn(i % rows, i / rows)));
  }

  implicit object DenseMatrixCanMapValuesFrom
  extends DomainMapCanMapValuesFrom[DenseMatrix,(Int,Int),Double,Double,DenseMatrix] {
    override def apply(from_ : DenseMatrix, fn : (Double=>Double)) = {
      val from : DenseMatrix = from_ // workaround for compiler crasher "scala.tools.nsc.symtab.Types$TypeError: value data$mcD$sp is not a member of type parameter From"
      val data = new Array[Double](from.data.length);
      var i = 0;
      while (i < data.length) {
        data(i) = fn(from.data(i));
        i += 1;
      }
      new DenseMatrix(from.numRows, from.numCols, data);
    }

    override def apply(from_ : DenseMatrix, fn : (((Int,Int),Double)=>Double)) = {
      val from : DenseMatrix = from_ // workaround for compiler crasher "scala.tools.nsc.symtab.Types$TypeError: value data$mcD$sp is not a member of type parameter From"
      val data = new Array[Double](from.data.length);
      var i = 0;
      while (i < data.length) {
        data(i) = fn(from.unindex(i), from.data(i));
        i += 1;
      }
      new DenseMatrix(from.numRows, from.numCols, data);
    }
  }
}
