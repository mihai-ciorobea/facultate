using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using Catalyst.Data.Entities.CatalystTime;
using Catalyst.SharedComponents;
using System;
using Catalyst.Data.Entities;
using Catalyst.Utils;

namespace Catalyst.Data.Repositories.Comments
{
    public class CommentRepository : StatelessSiteRepositoryBase, ICommentRepository
    {
        public IList<Comment> GetCommentsByType(int siteId, int objectId, Common.Types objectType)
        {
            IList<Comment> result = SitesDatabase(siteId).Execute
            (
                "spr_CMS_RatingUsageRetrieveByObject",
                CreateInstance,
                SiteIdSqlParameter(siteId),
                new SqlParameter("@ObjectID", objectId),
                new SqlParameter("@ObjectType", objectType)
            );

            return result;
        }

        public void UpdateCommentBody(Comment comment)
        {
            var siteId = comment.SiteId;
            SitesDatabase(siteId).ExecuteNonQuery
            (
                "spr_CMS_RatingUsageUpdateFeedback",
                SiteIdSqlParameter(siteId),
                new SqlParameter("@ObjectID", comment.ObjectId),
                new SqlParameter("@UsageID", comment.Id),
                new SqlParameter("@Feedback", comment.Feedback)
            );
        }

        public void UpdateCommentBaseInfo(Comment comment)
        {
            var entityId = comment.EntityId > 0 ? (object)comment.EntityId : DBNull.Value;
            var siteId = comment.SiteId;

            SitesDatabase(siteId).ExecuteNonQuery
            (
                "spr_CMS_RatingUsageUpdate",
                SiteIdSqlParameter(siteId),
                new SqlParameter("@UsageID", comment.Id),
                new SqlParameter("@ObjectID", comment.ObjectId),
                new SqlParameter("@ObjectType", (int)comment.ObjectType),
                new SqlParameter("@Feedback", comment.Feedback),
                new SqlParameter("@Rating", comment.Rating),
                new SqlParameter("@Website", comment.Website),
                new SqlParameter("@Status", comment.Status),
                new SqlParameter("@EntityId", entityId)
            );
        }

        public void UpdateCommentStatus(Comment comment)
        {
            var siteId = comment.SiteId;
            SitesDatabase(siteId).ExecuteNonQuery
            (
                "spr_CMS_RatingUsageUpdateStatus",
                SiteIdSqlParameter(siteId),
                new SqlParameter("@ObjectID", comment.ObjectId),
                new SqlParameter("@UsageID", comment.Id),
                new SqlParameter("@ObjectType", (int)comment.ObjectType),
                new SqlParameter("@Status", comment.Status)
            );
        }

        public void UpdateCommentStatus(int siteId, IList<Comment> comments)
        {
            var dt = new DataTable();
            dt.Columns.Add(new DataColumn("SiteID", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectType", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectID", typeof(int)));
            dt.Columns.Add(new DataColumn("UsageID", typeof(int)));
            dt.Columns.Add(new DataColumn("Status", typeof(int)));
            dt.Columns.Add(new DataColumn("NotificationSent", typeof(bool)));

            foreach (Comment comment in comments)
            {
                if (comment.SiteId == siteId)
                    dt.Rows.Add(comment.SiteId, comment.ObjectType, comment.ObjectId, comment.Id, comment.Status, null);
            }

            SitesDatabase(siteId).ExecuteNonQuery
            (
                "spr_CMS_RatingUsageUpdateStatusBulk",
                 new SqlParameter("@RatingUsageList", SqlDbType.Structured)
                 {
                     Direction = ParameterDirection.Input,
                     TypeName = "RatingUsageList",
                     Value = dt
                 }
            );
        }

        public void UpdateCommentNotification(SitesDatabase sitesDatabase, IList<Comment> comments)
        {
            var dt = new DataTable();
            dt.Columns.Add(new DataColumn("SiteID", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectType", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectID", typeof(int)));
            dt.Columns.Add(new DataColumn("UsageID", typeof(int)));
            dt.Columns.Add(new DataColumn("Status", typeof(int)));
            dt.Columns.Add(new DataColumn("NotificationSent", typeof(bool)));

            foreach (Comment comment in comments)
                dt.Rows.Add(comment.SiteId, comment.ObjectType, comment.ObjectId, comment.Id, null, comment.NotificationSent);

            sitesDatabase.ExecuteNonQuery
            (
                "spr_CMS_RatingUsageUpdateNotificationBulk",
                 new SqlParameter("@RatingUsageList", SqlDbType.Structured)
                 {
                     Direction = ParameterDirection.Input,
                     TypeName = "RatingUsageList",
                     Value = dt
                 }
            );
        }

        public void UpdateStatusForCommentQueueProcessed(SitesDatabase sitesDatabase, IList<CommentQueue> commentQueueEntities)
        {
            var dt = new DataTable();
            dt.Columns.Add(new DataColumn("QueueId", typeof(int)));
            dt.Columns.Add(new DataColumn("SiteID", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectType", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectID", typeof(int)));
            dt.Columns.Add(new DataColumn("UsageID", typeof(int)));
            dt.Columns.Add(new DataColumn("Status", typeof(int)));

            foreach (var cq in commentQueueEntities)
                dt.Rows.Add(cq.QueueId, cq.Comment.SiteId, cq.Comment.ObjectType, cq.Comment.ObjectId, cq.Comment.Id, cq.Comment.Status);

            sitesDatabase.ExecuteNonQuery
            (
                "spr_CMS_RatingUsageUpdateStatusForCommentQueueProcessedBulk",
                 new SqlParameter("@RatingUsageList", SqlDbType.Structured)
                 {
                     Direction = ParameterDirection.Input,
                     TypeName = "RatingUsageSpamProcessingTVP",
                     Value = dt
                 }
            );
        }

        public IList<Comment> GetBasicCommentInfo(int siteId, IList<Comment> comments)
        {
            var dt = new DataTable();
            dt.Columns.Add(new DataColumn("SiteID", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectType", typeof(int)));
            dt.Columns.Add(new DataColumn("ObjectID", typeof(int)));
            dt.Columns.Add(new DataColumn("UsageID", typeof(int)));
            dt.Columns.Add(new DataColumn("Status", typeof(int)));
            dt.Columns.Add(new DataColumn("NotificationSent", typeof(bool)));

            foreach (Comment comment in comments)
            {
                if (comment.SiteId == siteId)
                    dt.Rows.Add(comment.SiteId, comment.ObjectType, comment.ObjectId, comment.Id, null, null);
            }

            return SitesDatabase(siteId).Execute
            (
                "spr_CMS_RatringUsageGetStatusAndNotificationBulk",
                CreateBasicCommentInfoInstance,
                new SqlParameter("@RatingUsageList", SqlDbType.Structured)
                {
                    Direction = ParameterDirection.Input,
                    TypeName = "RatingUsageList",
                    Value = dt
                }
            );
        }

        public void Create(Comment comment)
        {
            var siteId = comment.SiteId;
            var entityId = comment.EntityId > 0 ? (object)comment.EntityId : DBNull.Value;
            var anonymousID = !string.IsNullOrEmpty(comment.AnonymousId) ? (object)comment.AnonymousId : DBNull.Value;
            var visitId = !string.IsNullOrEmpty(comment.VisitId) ? (object)comment.VisitId : DBNull.Value;
            var serverName = !string.IsNullOrEmpty(comment.ServerName) ? (object)comment.ServerName : DBNull.Value;
            var remoteAddress = !string.IsNullOrEmpty(comment.RemoteAddress) ? (object)comment.RemoteAddress : DBNull.Value;
            var remoteHost = !string.IsNullOrEmpty(comment.RemoteHost) ? (object)comment.RemoteHost : DBNull.Value;
            var httpReferer = !string.IsNullOrEmpty(comment.HttpReferer) ? (object)comment.HttpReferer : DBNull.Value;
            var httpUserAgent = !string.IsNullOrEmpty(comment.HttpUserAgent) ? (object)comment.HttpUserAgent : DBNull.Value;

            comment.UsageDate = DateTime.Now;

            comment.Id = Convert.ToInt32(SitesDatabase(siteId).ExecuteScalar("spr_CMS_RatingUsageCreate",
                SiteIdSqlParameter(siteId),
                new SqlParameter("@ObjectID", comment.ObjectId),
                new SqlParameter("@ObjectType", (int)comment.ObjectType),
                new SqlParameter("@UsageDate", comment.UsageDate),
                new SqlParameter("@Rating", comment.Rating),
                new SqlParameter("@Feedback", comment.Feedback),
                new SqlParameter("@Website", comment.Website),
                new SqlParameter("@AnonymousID", anonymousID),
                new SqlParameter("@VisitID", visitId),
                new SqlParameter("@SERVER_NAME", serverName),
                new SqlParameter("@REMOTE_ADDR", remoteAddress),
                new SqlParameter("@REMOTE_HOST", remoteHost),
                new SqlParameter("@HTTP_REFERER", httpReferer),
                new SqlParameter("@HTTP_USER_AGENT", httpUserAgent),
                new SqlParameter("@Status", comment.Status),
                new SqlParameter("@CountryCode", comment.CountryCode),
                new SqlParameter("@EntityID", entityId),
                new SqlParameter("@GenericEntityId", comment.GenericEntityId),
                new SqlParameter("@NotificationSent", comment.NotificationSent),
                new SqlParameter("@Hash", comment.Hash)
                ));
        }

        public int GetCommentCountForEntity(int siteId, int entityId, int objectId, Common.Types objectType)
        {
            return (int)SitesDatabase(siteId).ExecuteScalar
            (
                "spr_CMS_RatingUsageCountRetrieve",
                SiteIdSqlParameter(siteId),
                new SqlParameter("@EntityID", entityId),
                new SqlParameter("@ObjectID", objectId),
                new SqlParameter("@ObjectType", (int)objectType)
            );

        }

        public int GetApprovedCommentCount(int siteId, int objectId, Common.Types objectType)
        {
            return (int)SitesDatabase(siteId).ExecuteScalar
            (
                "spr_CMS_RatingFeedbackCount",
                SiteIdSqlParameter(siteId),
                new SqlParameter("@ObjectID", objectId),
                new SqlParameter("@ObjectType", (int)objectType)
            );

        }

        public double GetCommentRating(int siteId, int objectId, Common.Types objectType)
        {
            return Convert.ToDouble(
                SitesDatabase(siteId).ExecuteScalar
                (
                    "spr_CMS_RatingRankRetrieve",
                    SiteIdSqlParameter(siteId),
                    new SqlParameter("@ObjectID", objectId),
                    new SqlParameter("@ObjectType", (int)objectType)
                )
            );
        }


        internal static readonly ObjectCreator<Comment> CreateBasicCommentInfoInstance = r =>
           new Comment()
           {
               SiteId = Convert.ToInt32(r["SiteId"]),
               Id = Convert.ToInt32(r["UsageID"]),
               ObjectId = Convert.ToInt32(r["ObjectId"]),
               ObjectType = (Common.Types)Convert.ToInt32(r["ObjectType"]),
               UsageDate = Convert.ToDateTime(r["UsageDate"]),
               RemoteAddress = r["REMOTE_ADDR"] == DBNull.Value ? "" : r["REMOTE_ADDR"].ToString(),
               Status = (Common.CommentStatus)Convert.ToInt32(r["Status"]),
               NotificationSent = r["NotificationSent"] == DBNull.Value || Convert.ToBoolean(r["NotificationSent"])
           };

        public DataSet RatingUsageRetrive(int siteId, int objectId, int objectTypeId, Common.CommentStatus? status, TimeInterval<ServerTime> interval)
        {
            var siteIdSqlParam = SiteIdSqlParameter(siteId);
            var pars = new[]
                           {
                               siteIdSqlParam,
                               new SqlParameter("@ObjectType", objectTypeId),
                               new SqlParameter("@ObjectID", objectId),
                               new SqlParameter("@Status", (object)status ?? DBNull.Value),
                               new SqlParameter("@DateFrom", interval.DateFrom.InnerDateTime),
                               new SqlParameter("@DateTo", interval.DateTo.InnerDateTime)
                           };

            DataSet ds = null;

            ds = DAL.ExecuteDataSet((int)siteIdSqlParam.Value, "spr_CMS_RatingUsageRetrieve", pars);

            return ds;
        }


        public IList<Comment> RatingUsageRetrive(int siteId, int objectId, Common.Types objectType, string sortType, out int totalComments, int pageSize, int page)
        {
            var totalCommentsParam = new SqlParameter("@TotalComments", 0) { Direction = ParameterDirection.Output };

            IList<Comment> result = SitesDatabase(siteId).Execute
            (
                "spr_CMS_RatingFeedbackRetrieve",
                CreateInstance,
                SiteIdSqlParameter(siteId),
                new SqlParameter("@ObjectID", objectId),
                new SqlParameter("@ObjectType", objectType),
                new SqlParameter("@SortType", sortType),
                new SqlParameter("@RowCount", pageSize),
                new SqlParameter("@PageNumber", page),
                totalCommentsParam
            );

            totalComments = Convert.ToInt32(totalCommentsParam.Value);
            return result;
        }

        /// <summary>
        /// Creates a Comment instance out of a reader
        /// </summary>
        internal static Comment CreateInstance(IDataRecord r)
        {
            return new Comment
                {
                    SiteId = r.Get<int>("SiteId"),
                    Id = r.Get<int>("UsageID"),
                    FirstName = r.Get<string>("Firstname") ?? "",
                    LastName = r.Get<string>("Lastname") ?? "",
                    Rating = r.Get<int?>("Rating"),
                    Website = r.Get<string>("Website") ?? "",
                    Feedback = r.Get<string>("Feedback") ?? "",
                    UsageDate = r.Get<DateTime>("UsageDate"),
                    Status = r.Get<Common.CommentStatus>("Status")
                };

        }

        public IList<Comment> RatingUsageRetrivePendingForEntity(int siteId, int objectId, Common.Types objectType, int? entityId, Guid? genericEntityId, int rowCount = 0)
        {
            IList<Comment> result = SitesDatabase(siteId).Execute
            (
                "spr_CMS_RatingFeedbackRetrievePendingForEntity",
                CreateInstance,
                SiteIdSqlParameter(siteId),
                new SqlParameter("@ObjectID", objectId),
                new SqlParameter("@ObjectType", objectType),
                new SqlParameter("@RowCount", rowCount),
                new SqlParameter("@EntityId", (object)entityId ?? DBNull.Value),
                new SqlParameter("@GenericEntityId", (object)genericEntityId ?? DBNull.Value)
            );

            return result;
        }

        /// <summary>
        /// Adds an URL from a comment to the log, for statistical purposes
        /// </summary>
        /// <param name="url"></param>
        public void LogCommentUrl(string url)
        {
            if (!string.IsNullOrEmpty(url))
            {
                Database.Logging.ExecuteNonQuery("spr_CommentUrl_Add", new SqlParameter("URL", url));
            }
        }
    }
}