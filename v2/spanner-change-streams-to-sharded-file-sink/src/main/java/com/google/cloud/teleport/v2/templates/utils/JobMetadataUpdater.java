/*
 * Copyright (C) 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.teleport.v2.templates.utils;

import com.google.cloud.teleport.v2.utils.DurationUtils;
import org.joda.time.Duration;
import org.joda.time.Instant;

/** Updates the job start and duration and per shard progress. */
public class JobMetadataUpdater {

  public static void writeStartAndDuration(
      String spannerProjectId,
      String metadataInstance,
      String metadataDatabase,
      String startString,
      String duration,
      String tableSuffix,
      String runId) {
    SpannerDao spannerDao =
        new SpannerDao(spannerProjectId, metadataInstance, metadataDatabase, tableSuffix);
    Duration size = DurationUtils.parseDuration(duration);
    Instant timestamp = Instant.parse(startString);
    // fixed windows start with nearest value divisible by duration
    Instant start =
        new Instant(timestamp.getMillis() - timestamp.plus(size).getMillis() % size.getMillis());
    spannerDao.writeStartAndDuration(start.toString(), duration, runId);
  }
}
