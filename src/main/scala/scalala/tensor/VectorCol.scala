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
package tensor;

import scalar.Scalar;

import domain.IndexDomain;

import scalala.generic.collection.{CanAppendColumns};
import scalala.operators._;

/**
 * Implementation trait for a row vector.
 *
 * @author dramage
 */
trait VectorColLike[@specialized(Int,Long,Float,Double) V, +This<:VectorCol[V]]
extends VectorLike[V,This] with Tensor1ColLike[Int,V,IndexDomain,This] {
  override def t : VectorRow[V] =
    new VectorRow.View(repr);
}

/**
 * A vector shaped as a row.
 *
 * @author dramage
 */
trait VectorCol[@specialized(Int,Long,Float,Double) B]
extends Vector[B] with Tensor1Col[Int,B] with VectorColLike[B,VectorCol[B]];

object VectorCol {
  class View[V](override val inner : Vector[V])
  extends VectorProxy[V,Vector[V]] with VectorCol[V]
  with VectorLike[V,View[V]] {
    override def repr : View[V] = this;
  }

//  implicit def canAppendMatrixColumns[V]
//  : CanAppendColumns[Bound[V],Matrix[V],Matrix[V]]
//  = new CanAppendColumns[Bound[V],Matrix[V],Matrix[V]] {
//    override def apply(a : Bound[V], b : Matrix[V]) = {
//      require(a.size == b.numRows, "Arguments must have same number of rows");
//      implicit val sv = a.scalar;
//      val builder = a.newBuilder[(Int,Int),V](TableDomain(a.size, 1+b.numCols));
//      a.foreachNonZero((i,v) => builder((i,0)) = v);
//      b.foreachNonZero((i,j,v) => builder((i,j+1)) = v);
//      builder.result.asInstanceOf[Matrix[V]];
//    }
//  }

//  implicit def canAppendVectorColumn[V]
//  : CanAppendColumns[Bound[V],VectorCol[V],Matrix[V]]
//  = new CanAppendColumns[Bound[V],VectorCol[V],Matrix[V]] {
//    override def apply(a : Bound[V], b : VectorCol[V]) = {
//      require(a.size == b.size, "Arguments must have same number of rows");
//      implicit val sv = a.scalar;
//      val builder = a.newBuilder[(Int,Int),V](TableDomain(a.size, 2));
//      a.foreachNonZero((i,v) => builder((i,0)) = v);
//      b.foreachNonZero((i,v) => builder((i,1)) = v);
//      builder.result.asInstanceOf[Matrix[V]];
//    }
//  }
}

