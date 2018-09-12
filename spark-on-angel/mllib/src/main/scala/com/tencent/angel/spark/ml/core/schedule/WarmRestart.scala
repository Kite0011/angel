/*
 * Tencent is pleased to support the open source community by making Angel available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/Apache-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.tencent.angel.spark.ml.core.schedule

class WarmRestart(var etaMax: Double, etaMin: Double, var interval: Int, val decay: Double) extends StepSizeScheduler {

  var current: Int = 0
  var numRestart: Int = 0
  override def next(): Double = {
    current += 1
    val value = etaMin + 0.5 * (etaMax - etaMin) * (1 + math.cos(((current * 1.0) / interval * math.Pi)))
    if (current == interval) {
      current = 0
      interval *= 2
      numRestart += 1
      etaMax = etaMax / math.sqrt(1.0 + numRestart * decay)
    }
    return value
  }

  override
  def isIntervalBoundary(): Boolean = {
    return current == 0
  }

}
