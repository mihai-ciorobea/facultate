using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using Catalyst.Data.Entities;
using Catalyst.Entities.Customers;
using Catalyst.SharedComponents;

namespace Catalyst.Data.Repositories.Comments
{
    public class CommentQueueRepository : StatelessSiteRepositoryBase, ICommentQueueRepository
    {
        public void Insert(IList<CommentQueue> commentQueueList)
        {
            var dt = new DataTable();
            dt.Columns.Add(new DataColumn("QueueId", typeof(int)));
            dt.Columns.Add(new DataColumn("SiteId", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectType", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectID", typeof(int)));
            dt.Columns.Add(new DataColumn("UsageID", typeof(int)));
            dt.Columns.Add(new DataColumn("ActionTime", typeof(DateTime)));
            dt.Columns.Add(new DataColumn("Action", typeof(int)));
            dt.Columns.Add(new DataColumn("RetryCount", typeof(int)));

            var allGroups = commentQueueList.GroupBy(commentQueue => commentQueue.Comment.SiteId);

            foreach (var oneGroup in allGroups)
            {
                foreach (var commentQueue in oneGroup)
                {
                    dt.Rows.Add(null, commentQueue.Comment.SiteId, commentQueue.Comment.ObjectType,
                        commentQueue.Comment.ObjectId, commentQueue.Comment.Id, commentQueue.ActionTime, (int)commentQueue.Action, 0);
                }

                var parameters = new SqlParameter[1]
                                        {
                                            new SqlParameter("@Comments", SqlDbType.Structured)
                                                {
                                                    Direction = ParameterDirection.Input,
                                                    TypeName = "RatingUsageQueueProcessingTVP",
                                                    Value = dt
                                                }
                                        };

                DAL.ExecuteNonQuery(oneGroup.First().Comment.SiteId, "spr_CMS_Rating_Usage_QueueInsert", parameters);

                dt.Rows.Clear();
            }
        }

        public void InsertIfActionDoesntExists(SitesDatabase oneSiteDb, IList<CommentQueue> commentQueueEntities)
        {
            var dt = new DataTable();
            dt.Columns.Add(new DataColumn("QueueId", typeof(int)));
            dt.Columns.Add(new DataColumn("SiteId", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectType", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectID", typeof(int)));
            dt.Columns.Add(new DataColumn("UsageID", typeof(int)));
            dt.Columns.Add(new DataColumn("ActionTime", typeof(DateTime)));
            dt.Columns.Add(new DataColumn("Action", typeof(int)));
            dt.Columns.Add(new DataColumn("RetryCount", typeof(int)));

            foreach (var cq in commentQueueEntities)
                dt.Rows.Add(null, cq.Comment.SiteId, cq.Comment.ObjectType, cq.Comment.ObjectId, cq.Comment.Id, cq.ActionTime, (int)cq.Action, 0);

            oneSiteDb.ExecuteNonQuery
            (
                "spr_CMS_Rating_Usage_QueueInsertIfActionDoesntExists",
                 new SqlParameter("@Comments", SqlDbType.Structured)
                 {
                     Direction = ParameterDirection.Input,
                     TypeName = "RatingUsageQueueProcessingTVP",
                     Value = dt
                 }
            );
        }

        public void InsertForCommentQueueProcessed(SitesDatabase oneSiteDb, List<CommentQueue> commentQueueEntities)
        {
            var dt = new DataTable();
            dt.Columns.Add(new DataColumn("QueueId", typeof(int)));
            dt.Columns.Add(new DataColumn("SiteId", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectType", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectID", typeof(int)));
            dt.Columns.Add(new DataColumn("UsageID", typeof(int)));
            dt.Columns.Add(new DataColumn("ActionTime", typeof(DateTime)));
            dt.Columns.Add(new DataColumn("Action", typeof(int)));
            dt.Columns.Add(new DataColumn("RetryCount", typeof(int)));

            foreach (var cq in commentQueueEntities)
                dt.Rows.Add(cq.QueueId, cq.Comment.SiteId, cq.Comment.ObjectType, cq.Comment.ObjectId, cq.Comment.Id, cq.ActionTime, (int)cq.Action, 0);

            oneSiteDb.ExecuteNonQuery
            (
                "spr_CMS_Rating_Usage_QueueInsertForCommentQueueProcessed",
                 new SqlParameter("@Comments", SqlDbType.Structured)
                 {
                     Direction = ParameterDirection.Input,
                     TypeName = "RatingUsageQueueProcessingTVP",
                     Value = dt
                 }
            );
        }

        public void Delete(IList<CommentQueue> commentQueueList)
        {
            var dt = new DataTable();
            dt.Columns.Add(new DataColumn("QueueId", typeof(int)));
            dt.Columns.Add(new DataColumn("SiteId", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectType", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectID", typeof(int)));
            dt.Columns.Add(new DataColumn("UsageID", typeof(int)));
            dt.Columns.Add(new DataColumn("ActionTime", typeof(DateTime)));
            dt.Columns.Add(new DataColumn("Action", typeof(int)));
            dt.Columns.Add(new DataColumn("RetryCount", typeof(int)));

            var allGroups = commentQueueList.GroupBy(commentQueue => commentQueue.Comment.SiteId);

            foreach (var oneGroup in allGroups)
            {
                foreach (var commentQueue in oneGroup)
                {
                    dt.Rows.Add(null, commentQueue.Comment.SiteId, commentQueue.Comment.ObjectType, commentQueue.Comment.ObjectId, commentQueue.Comment.Id, null, (int)commentQueue.Action, null);
                }

                var parameters = new SqlParameter[1]
                                        {
                                            new SqlParameter("@Comments", SqlDbType.Structured)
                                                {
                                                    Direction = ParameterDirection.Input,
                                                    TypeName = "RatingUsageQueueProcessingTVP",
                                                    Value = dt
                                                }
                                        };

                SitesDatabase(oneGroup.First().Comment.SiteId).ExecuteNonQuery
                (
                    "spr_CMS_Rating_Usage_QueueDelete",
                    parameters
                );

                dt.Rows.Clear();
            }
        }

        public void DeleteByQueueId(SitesDatabase sitesDatabase, IList<CommentQueue> commentQueueEntities)
        {
            var dt = new DataTable();
            dt.Columns.Add(new DataColumn("n", typeof(int)));

            foreach (CommentQueue commentQueueEntity in commentQueueEntities)
                dt.Rows.Add(commentQueueEntity.QueueId);

            sitesDatabase.ExecuteNonQuery
             (
                 "spr_CMS_Rating_Usage_QueueDeleteByQueueId",
                  new SqlParameter("@QueueIds", SqlDbType.Structured)
                  {
                      Direction = ParameterDirection.Input,
                      TypeName = "IntegerListTVP",
                      Value = dt
                  }
             );




        }

        public void UpdateRetryCount(SitesDatabase sitesDatabase, IList<CommentQueue> commentQueueEntities)
        {
            var dt = new DataTable();
            dt.Columns.Add(new DataColumn("QueueId", typeof(int)));
            dt.Columns.Add(new DataColumn("SiteId", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectType", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectID", typeof(int)));
            dt.Columns.Add(new DataColumn("UsageID", typeof(int)));
            dt.Columns.Add(new DataColumn("ActionTime", typeof(DateTime)));
            dt.Columns.Add(new DataColumn("Action", typeof(int)));
            dt.Columns.Add(new DataColumn("RetryCount", typeof(int)));

            foreach (var cq in commentQueueEntities)
                dt.Rows.Add(cq.QueueId, null, null, null, null, null, null, cq.RetryCount);

            sitesDatabase.ExecuteNonQuery
            (
                "spr_CMS_Rating_Usage_QueueUpdateRetryCount",
                 new SqlParameter("@Comments", SqlDbType.Structured)
                 {
                     Direction = ParameterDirection.Input,
                     TypeName = "RatingUsageQueueProcessingTVP",
                     Value = dt
                 }
            );
        }

        public IList<CommentQueue> Select(int siteId, DateTime actionTime, int topCount, int maxRetryCount)
        {
            return SitesDatabase(siteId).Execute
            (
                "spr_CMS_Rating_Usage_QueueSelect",
                CreateFromRecord,
                SiteIdSqlParameter(siteId),
                new SqlParameter("@ActionTime", actionTime),
                new SqlParameter("@TopCount", topCount),
                new SqlParameter("@MaxRetryCount", maxRetryCount)
            );
        }

        public IList<CommentQueue> SelectCommentsForSpamProcessing(SitesDatabase siteDb, DateTime actionTime, int topCount, int maxRetryCount)
        {
            return siteDb.Execute
                (
                    "spr_CMS_Rating_Usage_QueueSelectCommentsForSpamProcessing",
                    CreateCommentForSpamProcessingFromRecord,
                    new SqlParameter("@TopCount", topCount),
                    new SqlParameter("@ActionTime", actionTime),
                    new SqlParameter("@MaxRetryCount", maxRetryCount)
                );
        }

        public int? RetriveAndLockSiteForProcessing(SitesDatabase db, string machineName, DateTime actionTime, int inactivitySeconds, int maxRetryCount)
        {
            SqlParameter siteId = new SqlParameter("@SiteId", DBNull.Value);
            siteId.SqlDbType = SqlDbType.Int;
            siteId.Direction = ParameterDirection.Output;

            db.ExecuteNonQuery("spr_CMS_Rating_Usage_QueueDistribution_SelectSite",
                new SqlParameter("@MachineName", machineName) { SqlDbType = SqlDbType.NVarChar, Size = 256 },
                siteId,
                new SqlParameter("@ActionTime", actionTime),
                new SqlParameter("@InactivitySeconds", inactivitySeconds),
                new SqlParameter("@MaxRetryCount", maxRetryCount)
                );

            if (siteId.Value == DBNull.Value)
                return null;

            return (int?)siteId.Value;
        }

        public void UnlockSite(int siteId)
        {
            SitesDatabase(siteId).ExecuteNonQuery
            (
                "spr_CMS_Rating_Usage_QueueDistribution_DeleteSite",
                SiteIdSqlParameter(siteId)
            );
        }

        public void KeepAlive(int siteId, DateTime currentDate)
        {
            SitesDatabase(siteId).ExecuteNonQuery
            (
                "spr_CMS_Rating_Usage_QueueDistribution_KeepAlive",
                SiteIdSqlParameter(siteId),
                  new SqlParameter("@CurrentDate", currentDate)
            );
        }


        public static CommentQueue CreateFromRecord(IDataRecord record)
        {
            // load company information
            CommentQueue result = new CommentQueue
            {
                QueueId = (Int32)(record["QueueId"]),
                Comment = new Comment()
                {
                    SiteId = (Int32)(record["SiteId"]),
                    ObjectType = (Common.Types)(record["ObjectType"]),
                    ObjectId = (Int32)(record["ObjectId"]),
                    Id = (Int32)(record["UsageID"]),
                    Status = (Common.CommentStatus)(record["Status"]),
                    NotificationSent = record["NotificationSent"] == DBNull.Value || (bool)(record["NotificationSent"]),
                    UsageDate = (DateTime)(record["UsageDate"]),
                    FirstName = record["FirstName"] == DBNull.Value ? null : record["FirstName"].ToString(),
                    LastName = record["LastName"] == DBNull.Value ? null : record["LastName"].ToString(),
                    Email = record["Email"] == DBNull.Value ? null : record["Email"].ToString(),
                    Feedback = record["Feedback"] == DBNull.Value ? string.Empty : record["Feedback"].ToString()
                },
                RetryCount = record["RetryCount"] == DBNull.Value ? 0 : (Int32)(record["RetryCount"]),
                Customer = null,
                Action = (Common.CommentQueueAction)(Int32)(record["Action"])
            };


            if (record["EntityId"] != DBNull.Value)
                result.Customer = new Customer()
                {
                    SiteId = (Int32)(record["SiteId"]),
                    CustomerId = (Int32)(record["EntityId"])
                };

            return result;
        }

        public static CommentQueue CreateCommentForSpamProcessingFromRecord(IDataRecord record)
        {
            return new CommentQueue
            {
                QueueId = (Int32)(record["QueueId"]),
                Comment = new Comment
                {
                    Id = (Int32)(record["UsageID"]),
                    SiteId = (Int32)(record["SiteId"]),
                    ObjectId = (Int32)(record["ObjectId"]),
                    ObjectType = (Common.Types)(record["ObjectType"]),
                    UsageDate = (DateTime)(record["UsageDate"]),
                    HttpUserAgent = record["HTTP_USER_AGENT"] == DBNull.Value ? string.Empty : (string)(record["HTTP_USER_AGENT"]),
                    HttpReferer = record["HTTP_REFERER"] == DBNull.Value ? string.Empty : (string)(record["HTTP_REFERER"]),
                    Feedback = record["Feedback"] == DBNull.Value ? string.Empty : (string)(record["Feedback"]),
                    FirstName = record["FirstName"] == DBNull.Value ? string.Empty : (string)(record["FirstName"]),
                    LastName = record["LastName"] == DBNull.Value ? string.Empty : (string)(record["LastName"]),
                    RemoteAddress = record["REMOTE_ADDR"] == DBNull.Value ? string.Empty : (string)(record["REMOTE_ADDR"]),
                    Email = record["Email"] == DBNull.Value ? string.Empty : (string)(record["Email"]),
                    Status = (Common.CommentStatus)(record["Status"]),
                    Website = record["Website"] == DBNull.Value ? null : (string)(record["Website"]),
                    NotificationSent = record["NotificationSent"] == DBNull.Value || (bool)(record["NotificationSent"])
                },
                RetryCount = record["RetryCount"] == DBNull.Value ? 0 : (Int32)(record["RetryCount"]),
                Customer = null,
                Action = (Common.CommentQueueAction)(Int32)(record["Action"])
            };
        }
    }
}
